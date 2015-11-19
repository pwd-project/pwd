package org.pwd.web.download;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import org.pwd.domain.contact.HtmlEmailMessage;
import org.pwd.domain.download.DownloadRequest;
import org.pwd.domain.download.DownloadRequestRepository;
import org.pwd.domain.download.Template;
import org.pwd.interfaces.mailgun.MailgunClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
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

    private static Template template;
    private static String cms;

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

        template = Template.valueOf(templateName);
        cms = cmsName;
        model.addAttribute("template", template);
        model.addAttribute("cms", cms);
        return "downloadForm";
    }

    @RequestMapping(method = POST)
    public String sendEmail(DownloadRequest downloadRequest) {
        downloadRequest.setTemplateName(template.getNamePl());
        downloadRequest.setCms(cms);
        downloadRequest.setCreated(LocalDateTime.now());
        logger.info("New download request: {}", downloadRequest);
        logger.info("Data: "+downloadRequest.getCreated().toString());

        downloadRequest = downloadRequestRepository.save(downloadRequest);

        HtmlEmailMessage htmlEmailMessage = new HtmlEmailMessage("noreply@pwd.dolinagubra.pl", downloadRequest.getAdministrativeEmail(),
                "Szablon CMS ze strony PWD",
                    getEmailMessageTemplate(cms),
                    getEmailMessageModelMap(template.getNamePl(), cms, template.getDownloadName()));

        if (mailgunClient.sendEmail(htmlEmailMessage)) {
            logger.info("Email {} was sent successfully", htmlEmailMessage);
            return "email_download";
        }
        logger.warn("Email {} could not be sent", htmlEmailMessage);
        return "error";
    }

    private JtwigTemplate getEmailMessageTemplate(String cms){
        return new JtwigTemplate(
                new ClasspathJtwigResource("templates/emails/"+cms+"/DownloadEmail.twig"),
                new JtwigConfiguration()
        );
    }

    private JtwigModelMap getEmailMessageModelMap(String name, String cms, String file) {
        return new JtwigModelMap()
                .withModelAttribute("name", name)
                .withModelAttribute("cms", cms)
                .withModelAttribute("file", file);
    }
}
