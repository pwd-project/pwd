package org.pwd.domain.contact;

public final class EmailMessage {
    private final String from;
    private final String to;
    private final String subject;
    private final String text;

    public EmailMessage(String from, String to, String subject, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
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

    public String getText() {
        return text;
    }
}
