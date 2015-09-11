package org.pwd.web.contact;

import com.google.common.base.Preconditions;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    @Autowired
    public ContactController(@Value("${smtp.mailbox}") String smtpMail,
                             @Value("${MAILGUN_API_KEY}") String apiKey,
                             @Value("${MAILGUN_DOMAIN}") String smtpUrl) {
        Preconditions.checkArgument(!smtpMail.isEmpty());
        Preconditions.checkArgument(!apiKey.isEmpty());
        Preconditions.checkArgument(!smtpUrl.isEmpty());

        this.smtpMail = smtpMail;

        if( !apiKey.startsWith("api:"))
            this.apiKey = "api:" + apiKey;
        else
            this.apiKey = apiKey;

        if( !smtpUrl.startsWith("http"))
            this.smtpUrl = "https://api.mailgun.net/v3/" + smtpUrl + "/messages";
        else
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

            MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String, String>();
            parameters.add("from", email);
            parameters.add("to", smtpMail);
            parameters.add("subject",SUBJECT);
            parameters.add("text", composeMessage(name, email, mobile, site, message));

            HttpEntity request = new HttpEntity<MultiValueMap<String, String>>(parameters,headers);

            ResponseEntity<String> response = restTemplate.exchange(smtpUrl,HttpMethod.POST, request, String.class);

            return "email_sent";

        }catch (RuntimeException mex) {
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
