package oop.tasks.impl;

import javax.mail.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailReceiverCore {
    private final String folderName = "INBOX";
    private final String protocol = "pop3s";
    public final int mode = Folder.READ_ONLY;
    public final boolean expunge = false;
    private final Map<ConfigProperties, String> serverGmailConfig;
    private final Map<ConfigProperties, String> receiverConfig;
    private final Properties properties;

    public MailReceiverCore(){
        this.serverGmailConfig = new HashMap<ConfigProperties, String>(){{
            put(ConfigProperties.HOST, "pop.gmail.com");
            put(ConfigProperties.PORT, "995");
            put(ConfigProperties.TLS, "true");
        }};
        this.receiverConfig = new HashMap<ConfigProperties, String>(){{
            put(ConfigProperties.HOST, "mail.pop3.host");
            put(ConfigProperties.PORT, "mail.pop3.port");
            put(ConfigProperties.TLS, "mail.pop3.starttls.enable");
        }};

        this.properties = createProperties();
    }

    private Properties createProperties() {
        Properties properties = new Properties();
        properties.put(receiverConfig.get(ConfigProperties.HOST), serverGmailConfig.get(ConfigProperties.HOST));
        properties.put(receiverConfig.get(ConfigProperties.PORT), serverGmailConfig.get(ConfigProperties.PORT));
        properties.put(receiverConfig.get(ConfigProperties.TLS), serverGmailConfig.get(ConfigProperties.TLS));
        return properties;
    }

    public List getLetters(String mail, String password, int messagesNumber) throws MessagingException {
        List<Message> resultMessages = new ArrayList<>(messagesNumber);

        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore(protocol);
        store.connect(serverGmailConfig.get(ConfigProperties.HOST), mail, password);
        Folder emailFolder = store.getFolder(folderName);
        emailFolder.open(mode);

        Message[] m = emailFolder.getMessages();
        for (int i = 0; i < messagesNumber; i++)
            resultMessages.add(m[i]);


        emailFolder.close(expunge);
        store.close();
        return resultMessages;
    }
}
