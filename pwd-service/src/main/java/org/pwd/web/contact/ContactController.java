package org.pwd.web.contact;

import org.pwd.infrastructure.EmailMessage;
import org.pwd.interfaces.mailgun.MailgunClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Sławomir Mikulski
 */
@Controller
@RequestMapping("/kontakt")
public class ContactController {

    public static final String NAME_LABEL = "Imię i Nazwisko:";
    public static final String EMAIL_LABEL = "Email:";
    public static final String MOBILE_PHONE_LABEL = "Numer telefonu:";
    public static final String SITE_URL_LABEL = "Adres strony:";
    public static final String MESSAGE_LABEL = "Wiadomość:";

    public static final String SUBJECT = "Zgłoszenie ze strony PWD";

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final MailgunClient mailgunClient;
    private final String mailbox;

    @Autowired
    public ContactController(MailgunClient mailgunClient, @Value("${mailgun.mailbox}") String mailbox) {
        this.mailgunClient = mailgunClient;
        this.mailbox = mailbox;
    }

    @RequestMapping(method = POST)
    public String sendEmail(@RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "email", required = true) String email,
                            @RequestParam(value = "mobile", required = false) String mobile,
                            @RequestParam(value = "site", required = false) String site,
                            @RequestParam(value = "message", required = false) String message) {

        try{
            EmailMessage emailMessage = new EmailMessage(email,mailbox,SUBJECT,composeMessage(name, email, mobile, site, message));

            if( mailgunClient.sendEmail(emailMessage) )
                return "email_sent";
            else
                return "error";

        }catch (RuntimeException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage()+"\n");

            return "error";
        }
    }

    private String composeMessage(String name, String email, String mobile, String site, String message){
        return NAME_LABEL+" "+name+"\n"
             + EMAIL_LABEL+" "+email+"\n"
             + MOBILE_PHONE_LABEL+" "+mobile+"\n"
             + SITE_URL_LABEL+" "+site+"\n"
             + MESSAGE_LABEL+" "+message;
    }
}
