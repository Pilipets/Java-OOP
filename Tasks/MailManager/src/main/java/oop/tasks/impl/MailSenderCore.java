package oop.tasks.impl;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MailSenderCore {
    private final Map<ConfigProperties, String> clientGmailConfig;
    private final Map<ConfigProperties, String> senderConfig;
    private final Properties properties;
    private String username;
    private String password;
    private Session session;

    public MailSenderCore(){
        this.senderConfig = new HashMap<ConfigProperties, String>(){{
            put(ConfigProperties.HOST, "mail.smtp.host");
            put(ConfigProperties.PORT, "mail.smtp.port");
            put(ConfigProperties.AUTH, "mail.smtp.auth");
            put(ConfigProperties.TLS, "mail.smtp.starttls.enable");
        }};
        this.clientGmailConfig = new HashMap<ConfigProperties, String>(){{
            put(ConfigProperties.HOST, "smtp.gmail.com");
            put(ConfigProperties.PORT, "587");
            put(ConfigProperties.TLS, "true");
            put(ConfigProperties.AUTH, "true");
        }};
        properties = createDefaultProperties();
    }

    private Properties createDefaultProperties() {
        Properties prop = new Properties();
        prop.put(senderConfig.get(ConfigProperties.HOST), clientGmailConfig.get(ConfigProperties.HOST));
        prop.put(senderConfig.get(ConfigProperties.PORT), clientGmailConfig.get(ConfigProperties.PORT));
        prop.put(senderConfig.get(ConfigProperties.AUTH),  clientGmailConfig.get(ConfigProperties.AUTH));
        prop.put(senderConfig.get(ConfigProperties.TLS),  clientGmailConfig.get(ConfigProperties.TLS));
        return prop;
    }

    public void setAuthenticationData(String username,String password){
        this.username = username;
        this.password = password;
    }

    private Session getSession(){
        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public void sendMessage(String receiverEmailAddress, String subject,String text) throws MessagingException {
        session = getSession();
        Message message = generateMessage(receiverEmailAddress,subject,text);
        Transport.send(message);
    }

    private Message generateMessage(String receiverEmailAddress, String subject,String text) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(receiverEmailAddress)
        );
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
