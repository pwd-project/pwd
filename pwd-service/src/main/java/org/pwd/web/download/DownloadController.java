package org.pwd.web.download;

import org.pwd.domain.download.DownloadRequest;
import org.pwd.domain.download.DownloadRequestRepository;
import org.pwd.domain.download.Template;
import org.pwd.infrastructure.EmailMessage;
import org.pwd.interfaces.mailgun.MailgunClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kadamowi
 */
@Controller
@RequestMapping("/pobierz")
public class DownloadController {

    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    private final MailgunClient mailgunClient;
    private final String mailbox;
    private final DownloadRequestRepository downloadRequestRepository;

    @Autowired
    public DownloadController(MailgunClient mailgunClient, @Value("${mailgun.mailbox}") String mailbox, DownloadRequestRepository downloadRequestRepository) {
        this.mailgunClient = mailgunClient;
        this.mailbox = mailbox;
        this.downloadRequestRepository = downloadRequestRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showTemplates(Model model) {
        model.addAttribute("templates", Arrays.asList(Template.values()));
        return "download";
    }

    @RequestMapping(value = "/{templateName}/{cmsName}", method = RequestMethod.GET)
    public String showTemplateDownloadForm(@PathVariable String templateName, @PathVariable String cmsName, Model model) {

        model.addAttribute("template", Template.valueOf(templateName));
        model.addAttribute("cms",cmsName);
        return "downloadForm";
    }

    @RequestMapping(method = POST)
    public String sendEmail(@RequestParam(value = "templateName", required = true) String name,
                            @RequestParam(value = "cms", required = true) String cms,
                            @RequestParam(value = "unit", required = true) String unit,
                            @RequestParam(value = "city", required = true) String city,
                            @RequestParam(value = "administrativeEmail", required = true) String email) {

        EmailMessage emailMessage = new EmailMessage(mailbox, email, "Szablon CMS ze strony PWD", composeMessage(name,cms));
        DownloadRequest downloadRequest = new DownloadRequest(name,cms,unit,city,email);
        downloadRequest = downloadRequestRepository.save(downloadRequest);
        logger.info("New created record id: {}",downloadRequest.getId());
        if (mailgunClient.sendEmail(emailMessage)) {
            logger.info("Email {} was sent successfully", emailMessage);
            return "email_download";
        }
        logger.warn("Email {} could not be sent", emailMessage);
        return "error";
    }

    private String composeMessage(String name, String cms) {
        logger.info("Szablon: {}",name+" "+cms);
        return String.format("Wybrałeś szablon %s do systemu %s\n Wkrótce udostępnimy szablony", name, cms);
    }
}
