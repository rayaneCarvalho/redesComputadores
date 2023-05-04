package com.example.redes.protocoloSmtp;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import jakarta.mail.MessagingException;

public class EmailClient {
    private EmailSender sender;
    private EmailReceiver receiver;

    public EmailClient(String smtpHost, int smtpPort, String pop3Host, int pop3Port) {
        this.sender = new EmailSender(smtpHost, smtpPort);
        this.receiver = new EmailReceiver(pop3Host, pop3Port);
    }

    public void setCredentials(String username, String password) {
        sender.setCredentials(username, password);
        receiver.setCredentials(username, password);
    }

    public void sendEmail() throws MessagingException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("From:");
        String from = scanner.nextLine();
        System.out.println("To:");
        String to = scanner.nextLine();
        System.out.println("Subject:");
        String subject = scanner.nextLine();
        System.out.println("Content:");
        String content = scanner.nextLine();
        Email email = new Email(from, to, subject, content);
        System.out.println("Attach file? (Y/N)");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("Y")) {
            System.out.println("File path:");
            String filePath = scanner.nextLine();
            File attachment = new File(filePath);
            email.addAttachment(attachment);
        }
        sender.sendEmail(email);
        System.out.println("Email sent successfully.");
    }

    public void receiveEmails() throws MessagingException {
        List<Email> emails = receiver.receiveEmails();
        for (Email email : emails) {
            System.out.println("From: " + email.getFrom());
            System.out.println("To: " + email.getTo());
            System.out.println("Subject: " + email.getSubject());
            System.out.println("Content: " + email.getContent());
            System.out.println("Attachments: " + email.getAttachments());
            System.out.println("------");
        }
    }

    public static void main(String[] args) throws MessagingException {
        EmailClient client = new EmailClient("smtp.gmail.com", 587, "pop.gmail.com", 995);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Username:");
        String username = scanner.nextLine();
        System.out.println("Password:");
        String password = scanner.nextLine();
        client.setCredentials(username, password);
        while (true) {
            System.out.println("Options:");
            System.out.println("1 - Send email");
            System.out.println("2 - Receive emails");
            System.out.println("3 - Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                client.sendEmail();
            } else if (choice == 2) {
                client.receiveEmails();
            } else if (choice == 3) {
                break;
            }
        }
    }
}
