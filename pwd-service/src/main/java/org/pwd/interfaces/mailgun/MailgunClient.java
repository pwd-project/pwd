package org.pwd.interfaces.mailgun;

import com.google.common.base.Preconditions;
import org.apache.tomcat.util.codec.binary.Base64;
import org.pwd.infrastructure.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sławomir Mikulski
 */
@Service
public class MailgunClient {
    private static final Logger logger = LoggerFactory.getLogger(MailgunClient.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers;
    private final String url;

    @Autowired
    public MailgunClient(@Value("${pwd.mailbox}") String mailbox,
                         @Value("${mailgun.apikey}") String apiKey,
                         @Value("${mailgun.domain}") String domain) {
        Preconditions.checkArgument(!mailbox.isEmpty());
        Preconditions.checkArgument(!apiKey.isEmpty());
        Preconditions.checkArgument(!domain.isEmpty());

        this.url = domain + "/messages";
        this.headers = new HttpHeaders();

        byte[] apiKeyBytes = apiKey.getBytes();
        byte[] base64ApiKeyBytes = Base64.encodeBase64(apiKeyBytes);
        String base64ApiKey = new String(base64ApiKeyBytes);

        headers.add("Authorization", "Basic " + base64ApiKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public boolean sendEmail(EmailMessage emailMessage) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("from", emailMessage.getFrom());
        formData.add("to", emailMessage.getTo());
        formData.add("subject", emailMessage.getSubject());
        formData.add("text", emailMessage.getText());
        HttpEntity request = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return (response.getStatusCode() == HttpStatus.OK);
        } catch (RestClientException e) {
            logger.info("mailgun request failed", e);
            return false;
        }
    }
}
