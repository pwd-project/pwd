package org.pwd.web.contact;

import com.google.common.base.Preconditions;
import org.apache.tomcat.util.codec.binary.Base64;
import org.pwd.infrastructure.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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

    private final String smtpMail;
    private final String apiKey;
    private final String smtpUrl;

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    public ContactController(@Value("${mailgun.mailbox}") String smtpMail,
                             @Value("${mailgun.apikey}") String apiKey,
                             @Value("${mailgun.domain}") String smtpUrl) {
        Preconditions.checkArgument(!smtpMail.isEmpty());
        Preconditions.checkArgument(!apiKey.isEmpty());
        Preconditions.checkArgument(!smtpUrl.isEmpty());

        this.smtpMail = smtpMail;
        this.apiKey = apiKey;
        this.smtpUrl = smtpUrl;
    }


    @RequestMapping(method = POST)
    public String sendEmail(@RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "email", required = true) String email,
                            @RequestParam(value = "mobile", required = false) String mobile,
                            @RequestParam(value = "site", required = false) String site,
                            @RequestParam(value = "message", required = false) String message) {

        try{
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            byte[] apiKeyBytes = apiKey.getBytes();
            byte[] base64ApiKeyBytes = Base64.encodeBase64(apiKeyBytes);
            String base64ApiKey = new String(base64ApiKeyBytes);

            headers.add("Authorization","Basic " + base64ApiKey );

            EmailMessage emailMessage = new EmailMessage(email,smtpMail,SUBJECT,composeMessage(name, email, mobile, site, message));

            HttpEntity request = new HttpEntity<EmailMessage>(emailMessage,headers);

            ResponseEntity<String> response = restTemplate.exchange(smtpUrl,HttpMethod.POST, request, String.class);
            if( response.getStatusCode() == HttpStatus.OK)
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
