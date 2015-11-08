package org.pwd.domain.contact;

public final class EmailMessage {
    private final String from;
    private final String to;
    private final String subject;
    private final String text;
    private final Boolean isHTML;

    public EmailMessage(String from, String to, String subject, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.isHTML = false;
    }

    public EmailMessage(String from, String to, String subject, String text, Boolean isHTML) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.isHTML = isHTML;
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

    public Boolean getIsHTML() { return isHTML; }
}
