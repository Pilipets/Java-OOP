import com.webbank.configuration.AppConfig;
import com.webbank.controller.AppController;
import com.webbank.dao.*;
import com.webbank.model.*;
import com.webbank.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.*;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class SpringTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    @Mock
    MessageSource messageSource;
    @Mock
    UserProfileServiceImpl userProfileService;
    @Mock
    SecurityServiceImpl securityService;
    @Mock
    AuthenticationTrustResolver authenticationTrustResolver;
    @Mock
    HttpServletRequest request;
    @Mock
    ModelMap model;
    @Mock
    BankDao bankDao;
    @Mock
    BankStatsDao bankStatsDao;
    @Spy
    BusinessServiceImpl businessService;
    @Mock
    AccountDao accountDao;


    @Spy
    @InjectMocks
    private AppController userController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();

        when(accountDao.getAccount(anyInt())).thenReturn(new Account());
        when(bankDao.getBankByAccount(anyInt())).thenReturn(new Bank());
        BankStats bankStats = new BankStats();
        bankStats.setTotalTransfer(BigInteger.ONE);
        bankStats.setTotalProfit(BigInteger.ONE);
        bankStats.setTotalReplenish(BigInteger.ONE);
        when(bankStatsDao.getBankStatsByBank(any(Bank.class))).thenReturn(bankStats);
        ReflectionTestUtils.setField(businessService, "accountDao", accountDao);
        ReflectionTestUtils.setField(businessService, "cardDao", mock(CardDao.class));
        ReflectionTestUtils.setField(businessService, "paymentDao", mock(PaymentDao.class));
        ReflectionTestUtils.setField(businessService, "bankDao", bankDao);
        ReflectionTestUtils.setField(businessService, "topupDao", mock(TopupDao.class));
        ReflectionTestUtils.setField(businessService, "bankStatsDao", bankStatsDao);
    }
    public void setSecurityContext(String role){
        UserProfile userProfile = new UserProfile();
        userProfile.setType(role);
        User user = new User();
        user.setUsername("xyz");
        Set<UserProfile> userProfileSet = new HashSet<>();
        userProfileSet.add(userProfile);
        user.setUserProfiles(userProfileSet);
        when(userService.findByUsername("xyz")).thenReturn(user);
        when(userProfileService.findByType("USER")).thenReturn(userProfile);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getPrincipal()).thenReturn("xyz");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

    }
    @Test
    public void submitRegistration() throws Exception {
        when(userService.isUserNameUnique("xyz")).thenReturn(true);
        when(userProfileService.findByType("USER")).thenReturn(new UserProfile());
        MockHttpServletResponse resp = this.mockMvc
                .perform(
                        post("/newuser")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", "xyz")
                                .param("username", "xyz")
                                .param("password", "xyz")
                )
                .andExpect(status().isOk()).andReturn().getResponse();
        Assert.assertEquals(resp.getForwardedUrl(), "login");
        verify(userService, times(1)).saveUser(any());
        verify(userProfileService, times(1)).findByType("USER");
        verify(userService, times(1)).isUserNameUnique("xyz");
    }
    @Test
    public void submitRegistrationWithWrongName() throws Exception {
        when(userService.isUserNameUnique("xyz")).thenReturn(false);
        MockHttpServletResponse resp = this.mockMvc
                .perform(
                        post("/newuser")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", "xyz")
                                .param("username", "xyz")
                                .param("password", "xyz")
                )
                .andExpect(status().isOk()).andReturn().getResponse();
        Assert.assertEquals(resp.getForwardedUrl(), "registration");
        verify(userService, times(1)).isUserNameUnique("xyz");
    }
    @Test
    public void loginUser() throws Exception {
        setSecurityContext("USER");

        this.mockMvc
                .perform(
                        get("/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", "xyz")
                                .param("password", "xyz")
                )
                .andExpect(redirectedUrl("/userPage"))
                .andExpect(status().is3xxRedirection());
        verify(userService, times(1)).findByUsername("xyz");
        verify(userController, times(1)).getPrincipal();
    }
    @Test
    public void loginAdmin() throws Exception {
        setSecurityContext("ADMIN");
        this.mockMvc
                .perform(
                        get("/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", "xyz")
                                .param("password", "xyz")
                )
                .andExpect(redirectedUrl("/adminPage"))
                .andExpect(status().is3xxRedirection());
        verify(userService, times(1)).findByUsername("xyz");
        verify(userController, times(1)).getPrincipal();
    }

    @Test
    public void PayNotEnoughMoney() throws Exception {
        setSecurityContext("USER");
        Map<Card, Account> cardAccountMap = new HashMap<>();
        Card card = new Card();
        card.setCardNumber(123);
        Account account = new Account();
        account.setMoneyAmount(1);
        cardAccountMap.put(card, account);
        MockHttpSession session = mock(MockHttpSession.class);
        when(session.getAttribute("cardAccountMap")).thenReturn(cardAccountMap);
        this.mockMvc
                .perform(
                        post("/operation")
                                .session(session)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("money", "10000")
                                .param("info", "info")
                                .param("card", "123")
                                .param("command", "Pay")
                )
                .andExpect(model().attribute("warning", "Warning: not enough money"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("userPage"));
    }
    @Test
    public void PayBlockedCard() throws Exception {
        setSecurityContext("USER");
        Map<Card, Account> cardAccountMap = new HashMap<>();
        Card card = new Card();
        card.setCardNumber(123);
        Account account = new Account();
        account.setMoneyAmount(10);
        cardAccountMap.put(card, account);
        MockHttpSession session = mock(MockHttpSession.class);
        when(session.getAttribute("cardAccountMap")).thenReturn(cardAccountMap);
        this.mockMvc
                .perform(
                        post("/operation")
                                .session(session)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("card", "123")
                                .param("command", "Block")
                )
                .andExpect(model().attribute("warning", "Warning: blocked"))
                .andExpect(status().isOk());
        this.mockMvc
                .perform(
                        post("/operation")
                                .session(session)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("money", "1")
                                .param("info", "info")
                                .param("card", "123")
                                .param("command", "Pay")
                )
                .andExpect(model().attribute("warning", "Warning: Account is blocked"))
                .andExpect(status().isOk());
    }
    @Test
    public void PaySuccess() throws Exception {
        setSecurityContext("USER");
        Map<Card, Account> cardAccountMap = new HashMap<>();
        Card card = new Card();
        card.setCardNumber(123);
        Account account = new Account();
        account.setMoneyAmount(10);
        cardAccountMap.put(card, account);
        MockHttpSession session = mock(MockHttpSession.class);
        when(session.getAttribute("cardAccountMap")).thenReturn(cardAccountMap);
        User user = new User();
        user.setUsername("xyz");
        user.setId(1);
        when(session.getAttribute("User")).thenReturn(user);
        when(session.getAttribute("cardAccountMap")).thenReturn(cardAccountMap);
        this.mockMvc
                .perform(
                        post("/operation")
                                .session(session)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("money", "1")
                                .param("info", "info")
                                .param("card", "123")
                                .param("command", "Pay")
                ).andExpect(status().isOk())
                .andExpect(model().attribute("testPayment", "info 1 9 123"));
    }
    @Test
    public void TopUpBlockedCard() throws Exception {
        setSecurityContext("USER");
        Map<Card, Account> cardAccountMap = new HashMap<>();
        Card card = new Card();
        card.setCardNumber(123);
        Account account = new Account();
        account.setMoneyAmount(10);
        cardAccountMap.put(card, account);
        MockHttpSession session = mock(MockHttpSession.class);
        when(session.getAttribute("cardAccountMap")).thenReturn(cardAccountMap);
        this.mockMvc
                .perform(
                        post("/operation")
                                .session(session)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("card", "123")
                                .param("command", "Block")
                )
                .andExpect(model().attribute("warning", "Warning: blocked"))
                .andExpect(status().isOk());
        this.mockMvc
                .perform(
                        post("/operation")
                                .session(session)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("money", "1")
                                .param("info", "info")
                                .param("card", "123")
                                .param("command", "TopUp")
                )
                .andExpect(model().attribute("warning", "Warning: Account is blocked"))
                .andExpect(status().isOk());
    }
    @Test
    public void TopUpSuccess() throws Exception {
        setSecurityContext("USER");
        Map<Card, Account> cardAccountMap = new HashMap<>();
        Card card = new Card();
        card.setCardNumber(123);
        Account account = new Account();
        account.setMoneyAmount(10);
        cardAccountMap.put(card, account);
        MockHttpSession session = mock(MockHttpSession.class);
        when(session.getAttribute("cardAccountMap")).thenReturn(cardAccountMap);
        User user = new User();
        user.setUsername("xyz");
        user.setId(1);
        when(session.getAttribute("User")).thenReturn(user);
        this.mockMvc
                .perform(
                        post("/operation")
                                .session(session)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("money", "1")
                                .param("info", "info")
                                .param("card", "123")
                                .param("command", "TopUp")
                )
                .andExpect(model().attribute("topup", "+1$ to account."))
                .andExpect(status().isOk());
    }
}
