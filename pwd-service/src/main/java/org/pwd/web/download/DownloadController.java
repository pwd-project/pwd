package org.pwd.web.download;

import org.pwd.domain.contact.PlainTextEmailMessage;
import org.pwd.domain.download.DownloadRequest;
import org.pwd.domain.download.DownloadRequestRepository;
import org.pwd.domain.download.Template;
import org.pwd.domain.contact.EmailMessage;
import org.pwd.interfaces.mailgun.MailgunClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private final DownloadRequestRepository downloadRequestRepository;

    @Autowired
    public DownloadController(MailgunClient mailgunClient, DownloadRequestRepository downloadRequestRepository) {
        this.mailgunClient = mailgunClient;
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
        model.addAttribute("cms", cmsName);
        return "downloadForm";
    }

    @RequestMapping(method = POST)
    public String sendEmail(DownloadRequest downloadRequest) {
        logger.info("New download request: {}", downloadRequest);
        downloadRequest = downloadRequestRepository.save(downloadRequest);

        PlainTextEmailMessage plainTextEmailMessage = new PlainTextEmailMessage("noreplay@pwd.dolinagubra.pl", downloadRequest.getAdministrativeEmail(),
                "Szablon CMS ze strony PWD", composeMessage(downloadRequest.getTemplateName(), downloadRequest.getCms()));

        if (mailgunClient.sendEmail(plainTextEmailMessage)) {
            logger.info("Email {} was sent successfully", plainTextEmailMessage);
            return "email_download";
        }
        logger.warn("Email {} could not be sent", plainTextEmailMessage);
        return "error";
    }

    private String composeMessage(String name, String cms) {
        return String.format("Wybrałeś szablon %s do systemu %s\nWkrótce udostępnimy szablony.", name, cms);
    }
}
