package mail_testing;

import mail_testing.src.Whitebox;
import oop.tasks.impl.MailSenderCore;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Properties;

public class SenderTest {
    static final MailSenderCore sender = new MailSenderCore();
    @Test
    public void checkConfigTest(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Assert.assertEquals(Whitebox.getInternalState(sender, "properties"),properties);
    }
}
