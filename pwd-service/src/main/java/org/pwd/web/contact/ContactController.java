package org.pwd.web.contact;

import org.pwd.domain.contact.ContactRequest;
import org.pwd.domain.contact.ContactRequestRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final MailgunClient mailgunClient;
    private final String mailbox;
    private final ContactRequestRepository contactRequestRepository;

    @Autowired
    public ContactController(MailgunClient mailgunClient, @Value("${mailgun.mailbox}") String mailbox, ContactRequestRepository contactRequestRepository) {
        this.mailgunClient = mailgunClient;
        this.mailbox = mailbox;
        this.contactRequestRepository = contactRequestRepository;
    }

    @RequestMapping(method = POST)
    public String sendEmail(@RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "administrativeEmail", required = true) String email,
                            @RequestParam(value = "mobile", required = false) String mobile,
                            @RequestParam(value = "site", required = false) String site,
                            @RequestParam(value = "message", required = false) String message,
                            @RequestParam(value = "emailh", required = false) String emailh) {

        if (!emailh.isEmpty()) {
            return "error";
        }

        EmailMessage emailMessage = new EmailMessage(email, mailbox, "Zgłoszenie ze strony PWD", composeMessage(name, email, mobile, site, message));
        ContactRequest contactRequest = new ContactRequest(name,email,mobile,site,message);
        contactRequest = contactRequestRepository.save(contactRequest);
        logger.info("New created record id: {}",contactRequest.getId());
        if (mailgunClient.sendEmail(emailMessage)) {
            logger.info("Email {} was sent successfully", emailMessage);
            return "email_sent";
        }
        logger.warn("Email {} could not be sent", emailMessage);
        return "error";
    }

    private String composeMessage(String name, String email, String mobile, String site, String message) {
        return String.format("%s\n%s\n%s\n%s\n%s", name, email, mobile, site, message);
    }
}
