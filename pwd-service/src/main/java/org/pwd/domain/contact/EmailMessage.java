package org.pwd.domain.contact;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EmailMessage {
    protected String from;
    protected String to;
    protected String subject;

    protected EmailMessage(){}

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
