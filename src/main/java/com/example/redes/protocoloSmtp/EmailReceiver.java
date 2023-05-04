package com.example.redes.protocoloSmtp;

import jakarta.websocket.Session;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static jdk.jpackage.internal.WixAppImageFragmentBuilder.Id.Folder;

public class EmailReceiver {
    private String host;
    private int port;
    private String username;
    private String password;
    private Properties props;

    public EmailReceiver(String host, int port) {
        this.host = host;
        this.port = port;
        this.props = new Properties();
        props.put("mail.pop3.host", host);
        props.put("mail.pop3.port", port);
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        props.put("mail.pop3.auth", "true");
        props.put("mail.pop3.starttls.enable", "true");
    }

    public List<Email> receiveEmails() throws MessagingException, IOException {
        List<Email> emails = new ArrayList<>();
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        Store store = session.getStore("pop3");
        store.connect(host, port, username, password);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message[] messages = inbox.getMessages();
        for (Message message : messages) {
            String from = InternetAddress.toString(message.getFrom());
            String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
            String subject = message.getSubject();
            String content = message.getContent().toString();
            Email email = new Email(from, to, subject, content);
            emails.add(email);
        }
        inbox.close(false);
        store.close();
        return emails;
    }
}

