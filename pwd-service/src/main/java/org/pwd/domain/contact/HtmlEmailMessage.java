package org.pwd.domain.contact;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sławomir Mikulski
 */
public class HtmlEmailMessage extends EmailMessage {
    private static final Logger logger = LoggerFactory.getLogger(EmailMessage.class);
    private final String html;

    public HtmlEmailMessage(String from, String to, String subject, JtwigTemplate jtwigTemplate, JtwigModelMap modelMap) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.html = renderTemplateIntoText(jtwigTemplate, modelMap);
    }

    public String getHtml() {
        return html;
    }

    private String renderTemplateIntoText(JtwigTemplate jtwigTemplate, JtwigModelMap modelMap) {
        String result;
        try {
            result = jtwigTemplate.output(modelMap);
        }
        catch (Exception ex){
            result = "";
            logger.error("Cannot render email text template: {}. Error: {}", jtwigTemplate, ex.getMessage());
        }
        return result;
    }
}
