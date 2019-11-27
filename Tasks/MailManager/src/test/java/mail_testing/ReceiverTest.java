package mail_testing;

import mail_testing.src.Whitebox;
import oop.tasks.impl.MailReceiverCore;
import org.junit.Assert;
import org.junit.Test;

import javax.mail.Folder;
import java.util.Properties;

public class ReceiverTest {
    static final MailReceiverCore receiver = new MailReceiverCore();
    @Test
    public void checkConfigTest(){
        Assert.assertEquals(Whitebox.getInternalState(receiver, "folderName"),"INBOX");
        Assert.assertEquals(Whitebox.getInternalState(receiver, "protocol"),"pop3s");
        Assert.assertEquals(Whitebox.getInternalState(receiver, "mode"), Folder.READ_ONLY);
        Assert.assertEquals(Whitebox.getInternalState(receiver, "expunge"), false);


        Properties properties = new Properties();
        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        Assert.assertEquals(Whitebox.getInternalState(receiver, "properties"), properties);
    }
}
