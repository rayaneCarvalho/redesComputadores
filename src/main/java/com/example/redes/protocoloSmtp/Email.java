package com.example.redes.protocoloSmtp;

import java.io.File;
import java.util.*;

public class Email {
    private String from;
    private String to;
    private String subject;
    private String content;
    private List<File> attachments;

    public Email(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.attachments = new ArrayList<>();
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public void addAttachment(File attachment) {
        attachments.add(attachment);
    }
}

