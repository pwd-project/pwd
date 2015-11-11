package org.pwd.domain.contact;

/**
 * @author Sławomir Mikulski
 */
public class PlainTextEmailMessage extends EmailMessage {
    private final String text;

    public PlainTextEmailMessage (String from, String to, String subject, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
