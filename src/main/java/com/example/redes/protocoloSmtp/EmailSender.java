package com.example.redes.protocoloSmtp;

import jakarta.websocket.Session;

import java.io.File;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailSender {
    private String host;
    private int port;
    private String username;
    private String password;
    private Properties props;

    public EmailSender(String host, int port) {
        this.host = host;
        this.port = port;
        this.props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }

    public void sendEmail(Email email) throws MessagingException {
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email.getFrom()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
        message.setSubject(email.getSubject());
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(email.getContent(), "text/plain");
        multipart.addBodyPart(messageBodyPart);
        List<File> attachments = email.getAttachments();
        for (File attachment : attachments) {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.attachFile(attachment);
            multipart.addBodyPart(messageBodyPart);
        }
        message.setContent(multipart);
        Transport.send(message);
    }
}

