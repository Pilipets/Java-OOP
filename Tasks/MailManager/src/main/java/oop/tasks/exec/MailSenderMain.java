package oop.tasks.exec;

import oop.tasks.impl.MailSenderCore;

import javax.mail.MessagingException;
import java.util.Scanner;

public class MailSenderMain {
    public static void main(String[] args) throws MessagingException {
        MailSenderCore sender = new MailSenderCore();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter you username : ");
        String username = scanner. nextLine();
        System.out.println("Enter app password : ");
        String password = scanner. nextLine();
        sender.setAuthenticationData(username, password);
        System.out.println("Enter receiver username : ");
        String receiverUsername = scanner. nextLine();
        System.out.println("Enter subject of mail : ");
        String subject = scanner. nextLine();
        System.out.println("Enter mail text : ");
        String text = scanner. nextLine();
        sender.sendMessage(receiverUsername, subject, text);
    }
}
