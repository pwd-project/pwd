package org.pwd.web.contact;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

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

    private final String smtpHost;
    private final String smtpUser;
    private final String smtpPass;
    private final String smtpMail;
    private final String smtpPort;

    @Autowired
    public ContactController(@Value("${smtp.host}") String smtpHost,
                             @Value("${smtp.username}") String smtpUser,
                             @Value("${smtp.password}") String smtpPass,
                             @Value("${smtp.mailbox}") String smtpMail,
                             @Value("${smtp.port}") String smtpPort) {
        Preconditions.checkArgument(!smtpHost.isEmpty());
        Preconditions.checkArgument(!smtpUser.isEmpty());
        Preconditions.checkArgument(!smtpPass.isEmpty());
        Preconditions.checkArgument(!smtpHost.isEmpty());
        Preconditions.checkArgument(!smtpPort.isEmpty());

        this.smtpHost = smtpHost;
        this.smtpUser = smtpUser;
        this.smtpPass = smtpPass;
        this.smtpMail = smtpMail;
        this.smtpPort = smtpPort;
    }


    @RequestMapping(method = POST)
    public String sendEmail(@RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "email", required = true) String email,
                            @RequestParam(value = "mobile", required = false) String mobile,
                            @RequestParam(value = "site", required = false) String site,
                            @RequestParam(value = "message", required = false) String message) {

        // Get system properties
        Properties properties = System.getProperties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.ssl.trust", smtpHost);

        // Get the Session object.
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUser, smtpPass);
                    }
                });

        try{
            // Create a default MimeMessage object.
            MimeMessage msg = new MimeMessage(session);

            // Set From: header field of the header.
            msg.setFrom(new InternetAddress(email));

            // Set To: header field of the header.
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(smtpMail));

            // Set Subject: header field
            msg.setSubject(SUBJECT);

            // Send the actual HTML message, as big as you like
            msg.setContent(composeMessage(name, email, mobile, site, message), "text/plain");

            // Send message
            Transport.send(msg);

            return "email_sent";

        }catch (MessagingException mex) {
            mex.printStackTrace();
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
