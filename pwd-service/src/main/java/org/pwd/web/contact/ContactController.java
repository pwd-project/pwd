package org.pwd.web.contact;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * @author Sławomir Mikulski
 */
@Controller
@RequestMapping("/kontakt")
public class ContactController {

    public static final String NAME_LABEL = "Imię i Nazwisko: ";
    public static final String EMAIL_LABEL = "Email: ";
    public static final String MOBILE_PHONE_LABEL = "Numer telefonu: ";
    public static final String SITE_URL_LABEL = "Adres strony: ";
    public static final String MESSAGE_LABEL = "Wiadomość: ";

    public static final String MAILBOX = "smikulsk@gmail.com";
    public static final String SUBJECT = "This is the Subject Line!";

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ContactController.class);

    @RequestMapping(method = POST)
    public String sendEmail(@RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "email", required = true) String email,
                            @RequestParam(value = "mobile", required = false) String mobile,
                            @RequestParam(value = "site", required = false) String site,
                            @RequestParam(value = "message", required = false) String message) {

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try{
            // Create a default MimeMessage object.
            MimeMessage msg = new MimeMessage(session);

            // Set From: header field of the header.
            msg.setFrom(new InternetAddress(email));

            // Set To: header field of the header.
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(MAILBOX));

            // Set Subject: header field
            message.setSubject(SUBJECT);

            // Send the actual HTML message, as big as you like
            message.setContent(composeMessage(name,email,mobile,site,message), "text/plain" );

            // Send message
            Transport.send(message);

            return "email_sent";

        }catch (MessagingException mex) {
            mex.printStackTrace();
            return "error";
        }
    }

    String composeMessage(String name, String email, String mobile, String site, String message){
        String result = "";

        result.append(NAME_LABEL); result.append(name); result.append("\n");
        result.append(EMAIL_LABEL); result.append(email); result.append("\n");
        result.append(MOBILE_PHONE_LABEL); result.append(mobile); result.append("\n");
        result.append(SITE_URL_LABEL); result.append(site); result.append("\n");
        result.append(MESSAGE_LABEL); result.append(message);

        return result;
    }
}
