package com.webbank.controller;

import com.webbank.model.*;
import com.webbank.service.BusinessServiceImpl;
import com.webbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/")
public class AppController {
   @Autowired
   private UserService userService;
   @Autowired
   private BusinessServiceImpl businessService;

   @GetMapping(value = { "/", "/userPage" })
   public String userPage(HttpServletRequest request) {
       User user = userService.getContextUser();
       for(UserProfile profile : user.getUserProfiles()){
           if(profile.getType().equals("ADMIN")){
               return "redirect:/adminPage";
           }
       }
       request.getSession().setAttribute("loggedinuser", user.getName());
       request.getSession().setAttribute("User", user);
       List<Card> cards = businessService.getUsersCards(user);


       List<Account> accounts = businessService.getAccounts(cards);
       Map<Card, Account> cardAccountMap = new HashMap<>();
       for (int i = 0; i < cards.size(); i++) {
           cardAccountMap.put(cards.get(i), accounts.get(i));
       }
       request.getSession().setAttribute("cardAccountMap", cardAccountMap);


       List<String> payments = new ArrayList<>();
       for(Payment payment : businessService.getPayments(user)) {
           payments.add("ID: " + payment.getId() + ". Info: " + payment.getInfo() +
                   ". Spended money: " + payment.getMoney() + ". Card: " + payment.getCard() + ". Date: " + payment.getDate());
       }
       request.getSession().setAttribute("payments", payments);

       return "userPage";
   }

    @GetMapping(value = {"/adminPage" })
    public String adminPage(HttpServletRequest request, ModelMap model) {
        User user = userService.getContextUser();
        request.getSession().setAttribute("loggedinuser", user.getName());
        Bank bank = businessService.getBank(user);
        List<Account> accounts = bank.getAccounts();
        List<Integer> accountsId = new ArrayList<>();
        for(Account account : accounts){
            accountsId.add(account.getId());
        }

        //get BankStats
        BankStats bankStats = businessService.getBankStats(bank);
        request.getSession().setAttribute("bankStats", bankStats);
        double koef = bankStats.getTotalReplenish().doubleValue()/
                bankStats.getTotalTransfer().doubleValue();
        DecimalFormat df = new DecimalFormat("#.###");
        Double statsKoef = Double.valueOf(df.format(koef));
        request.getSession().setAttribute("koef",
                statsKoef);
        request.getSession().setAttribute("topUpFee", bank.getTopUpFee());
        request.getSession().setAttribute("transferFee", bank.getTransferFee());
        // get BlockedCardsByAccounts
        request.getSession().setAttribute("blockedCards", businessService.getBlockedCards(accountsId));
        request.getSession().setAttribute("bankName", bank.getName());
        return "adminPage";
    }

    @GetMapping(value="/logout")
    public String logoutPage (HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }

    @PostMapping(value="/operation")
    private String makeOperation(HttpServletRequest request, ModelMap model) throws IOException {
        String commandName = request.getParameter("command");
        if (commandName == null) {
            model.addAttribute("warning", "wrong command");
        } else {
            Map<Card, Account> cardAccountMap =  (Map<Card, Account>)request.getSession().getAttribute("cardAccountMap");
            if (commandName.equals("Block")) {
                int cardNumber = Integer.valueOf(request.getParameter("card"));
                businessService.blockCommand(cardAccountMap, cardNumber, model);
            }
            else if (commandName.equals("TopUp")){
                int money = Integer.valueOf(request.getParameter("money"));
                int cardNumber = Integer.valueOf(request.getParameter("card"));
                businessService.topUpCommand(cardAccountMap, money, cardNumber, model);
            }
            else if(commandName.equals("Pay")){
                int money = Integer.valueOf(request.getParameter("money"));
                String info = request.getParameter("info");
                int cardNumber = Integer.valueOf(request.getParameter("card"));
                List<String> payments = (List<String>) request.getSession().getAttribute("payments");
                User user = (User)request.getSession().getAttribute("User");
                businessService.payCommand(cardAccountMap, payments, user, money, info, cardNumber, model);
            }
            else if (commandName.equals("Unblock")){
                int cardNumber = Integer.valueOf(request.getParameter("card"));
                List<Card> blockedCards = (List<Card>)request.getSession().getAttribute("blockedCards");
                businessService.unblockCommand(cardNumber, blockedCards);
            }
            else if (commandName.equals("Date")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateFrom = null;
                Date dateTo = null;
                try {
                    dateFrom = simpleDateFormat.parse(request.getParameter("dateFrom"));
                    dateTo = simpleDateFormat.parse(request.getParameter("dateTo"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int cardNumber = Integer.valueOf(request.getParameter("card"));
                businessService.dateCommand(dateFrom, dateTo, cardNumber, model);
            }
            else if (commandName.equals("DateStats")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateFrom;
                Date dateTo;
                try {
                    dateFrom = simpleDateFormat.parse(request.getParameter("dateFrom"));
                    dateTo = simpleDateFormat.parse(request.getParameter("dateTo"));
                    String bankName = request.getParameter("bankName");
                    businessService.dateStatsCommand(dateFrom, dateTo, bankName, model);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return "adminPage";
            }
        }
        User user = userService.getContextUser();
        for(UserProfile profile : user.getUserProfiles()){
            if(profile.getType().equals("ADMIN")){
                return "redirect:/adminPage";
            }
        }
        return "userPage";
    }
}