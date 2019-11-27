package oop.tasks.exec;

import oop.tasks.impl.MailReceiverCore;
import oop.tasks.impl.MailSenderCore;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Scanner;

public class MailReceiverMain {
    public static void main(String[] args) throws MessagingException {
        MailReceiverCore receiver = new MailReceiverCore();
        Scanner scanner = new Scanner( System. in);
        System.out.println("Enter you username : ");
        String username = scanner. nextLine();
        System.out.println("Enter app password : ");
        String password = scanner. nextLine();
        List<Message> messages = receiver.getLetters(username,password,30);
    }
}
