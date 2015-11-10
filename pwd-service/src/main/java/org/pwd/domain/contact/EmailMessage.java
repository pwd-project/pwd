package org.pwd.domain.contact;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EmailMessage {
    private static final Logger logger = LoggerFactory.getLogger(EmailMessage.class);

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

    public EmailMessage(String from, String to, String subject, JtwigTemplate jtwigTemplate, JtwigModelMap modelMap) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = renderTemplateIntoText(jtwigTemplate, modelMap);
        this.isHTML = this.text != "";
    }

    private String renderTemplateIntoText(JtwigTemplate jtwigTemplate, JtwigModelMap modelMap) {
        String result;
        try {
            result = jtwigTemplate.output(modelMap);
        }
        catch (CompileException ex){
            result = "";
            logger.error("Cannot compile email text template: {}. Error: {}", jtwigTemplate, ex.getMessage());
        }
        catch (ParseException ex){
            result = "";
            logger.error("Cannot parse email text template: {}. Error: {}", jtwigTemplate, ex.getMessage());
        }
        catch (RenderException ex){
            result = "";
            logger.error("Cannot render email text template: {}. Error: {}", jtwigTemplate, ex.getMessage());
        }
        return result;
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

    public Boolean getIsHTML(){
        return isHTML;
    }
}
