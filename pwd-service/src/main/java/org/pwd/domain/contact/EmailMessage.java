package org.pwd.domain.contact;

public abstract class EmailMessage {
    private final String from;
    private final String to;
    private final String subject;

    protected EmailMessage(String from, String to, String subject){
        this.from = from;
        this.to = to;
        this.subject = subject;
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
}
