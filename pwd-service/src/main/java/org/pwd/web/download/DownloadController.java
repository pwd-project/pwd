package org.pwd.web.download;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import org.pwd.domain.contact.HtmlEmailMessage;
import org.pwd.domain.download.Cms;
import org.pwd.domain.download.DownloadRequest;
import org.pwd.domain.download.DownloadRequestRepository;
import org.pwd.domain.download.Template;
import org.pwd.interfaces.mailgun.MailgunClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    private final HashFunction hasher = Hashing.md5();

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
        model.addAttribute("cms", Cms.valueOf(cmsName));
        return "downloadForm";
    }

    @RequestMapping(value = "/{templateName}/{cmsName}/{hashCode}/{timestamp}", method = RequestMethod.GET)
    public ResponseEntity<ClassPathResource> downloadTemplate(
            @PathVariable String templateName,
            @PathVariable String cmsName,
            @PathVariable String hashCode,
            @PathVariable long timestamp) {
        String validHash = getHash(templateName, cmsName, timestamp);
        if (validHash.equals(hashCode)) {
            long delay = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - timestamp;
            if (delay < 0 || delay > 3600) {
                throw new CmsTemplateHashExpiredException("Hash code: " + hashCode + " has expired. Delay: " + delay);
            }
            String filePath = "/pub/templates/" + cmsName + "/" + templateName + ".zip";
            ClassPathResource body = new ClassPathResource(filePath);
            return ResponseEntity.ok()
                    .header("content-disposition", "attachment; filename=" + body.getFilename())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(body);
        } else {
            throw new CmsTemplateHashErrorException("Hash code: " + hashCode + " is invalid.");
        }
    }

    @RequestMapping(value = "/{templateName}/{cmsName}", method = POST)
    public String sendEmail(DownloadRequest downloadRequest,
                            @PathVariable String templateName,
                            @PathVariable String cmsName) {
        downloadRequest.setTemplateName(templateName);
        downloadRequest.setCms(cmsName);
        downloadRequest.setCreated(LocalDateTime.now());
        logger.info("New download request: {}", downloadRequest);
        logger.info("Data: " + downloadRequest.getCreated().toString());
        downloadRequest = downloadRequestRepository.save(downloadRequest);
        long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        String hash = getHash(Template.valueOf(templateName).getDownloadName(), Cms.valueOf(cmsName).getNamePl(), timestamp);
        HtmlEmailMessage htmlEmailMessage = new HtmlEmailMessage("noreply@pwd.dolinagubra.pl", downloadRequest.getAdministrativeEmail(),
                "Szablon CMS ze strony PWD",
                getEmailMessageTemplate(),
                getEmailMessageModelMap(templateName, Template.valueOf(templateName).getDownloadName(), Cms.valueOf(cmsName).getNamePl(), hash, timestamp));
        if (mailgunClient.sendEmail(htmlEmailMessage)) {
            logger.info("Email {} was sent successfully", htmlEmailMessage);
            return "email_download";
        }
        logger.warn("Email {} could not be sent", htmlEmailMessage);
        return "error";
    }

    private String getHash(String templateName, String cms, long timestamp) {
        return hasher
                .newHasher()
                .putString(templateName, Charsets.UTF_8)
                .putString(cms, Charsets.UTF_8)
                .putLong(timestamp)
                .hash()
                .toString();
    }

    private JtwigTemplate getEmailMessageTemplate() {
        return new JtwigTemplate(
                new ClasspathJtwigResource("templates/emails/DownloadEmail.twig"),
                new JtwigConfiguration()
        );
    }

    private JtwigModelMap getEmailMessageModelMap(String name, String file, String cms, String hash, long timestamp) {
        return new JtwigModelMap()
                .withModelAttribute("name", name)
                .withModelAttribute("file", file)
                .withModelAttribute("cms", cms)
                .withModelAttribute("hash", hash)
                .withModelAttribute("timestamp", timestamp);
    }
}
