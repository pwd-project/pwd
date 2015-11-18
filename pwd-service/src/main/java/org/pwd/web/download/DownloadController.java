package org.pwd.web.download;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import org.pwd.domain.audit.Audit;
import org.pwd.domain.contact.HtmlEmailMessage;
import org.pwd.domain.contact.PlainTextEmailMessage;
import org.pwd.domain.download.DownloadRequest;
import org.pwd.domain.download.DownloadRequestRepository;
import org.pwd.domain.download.Template;
import org.pwd.domain.contact.EmailMessage;
import org.pwd.domain.websites.Website;
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

        HtmlEmailMessage htmlEmailMessage = new HtmlEmailMessage("noreply@pwd.dolinagubra.pl", downloadRequest.getAdministrativeEmail(),
                "Szablon CMS ze strony PWD", getEmailMessageTemplate(), getEmailMessageModelMap(downloadRequest.getTemplateName(), downloadRequest.getCms(), downloadRequest.getFile()));

        if (mailgunClient.sendEmail(htmlEmailMessage)) {
            logger.info("Email {} was sent successfully", htmlEmailMessage);
            return "email_download";
        }
        logger.warn("Email {} could not be sent", htmlEmailMessage);
        return "error";
    }

    private JtwigTemplate getEmailMessageTemplate(){
        return new JtwigTemplate(
                new ClasspathJtwigResource("templates/emails/DownloadEmail.twig"),
                new JtwigConfiguration()
        );
    }

    private JtwigModelMap getEmailMessageModelMap(String name, String cms, String file) {
        String path = "";
        switch (cms) {
            case "WordPress":
                path = Template.DOWNLOAD_PATH_WORDPRESS;
                break;
            case "Drupal":
                path = Template.DOWNLOAD_PATH_DRUPAL;
                break;
            case "Joomla":
                path = Template.DOWNLOAD_PATH_JOOMLA;
                break;
        }
        return new JtwigModelMap()
                .withModelAttribute("name", name)
                .withModelAttribute("cmd", cms)
                .withModelAttribute("path", path)
                .withModelAttribute("file", file);
    }
}
