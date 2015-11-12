package org.pwd.domain.contact;

/**
 * @author Sławomir Mikulski
 */
public class PlainTextEmailMessage extends EmailMessage {
    private final String text;

    public PlainTextEmailMessage (String from, String to, String subject, String text) {
        super(from,to,subject);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
