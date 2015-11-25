package org.pwd.web.download;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;

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
    private static Cms cms;

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
        try {
            template = Template.valueOf(templateName);
            cms = Cms.valueOf(cmsName);
        }
        catch(Exception ex) {
            logger.error(ex.getMessage());
            return "error_404";
        }
        model.addAttribute("template", template);
        model.addAttribute("cms", cms);
        return "downloadForm";
    }

    @RequestMapping(value = "/{templateName}/{cmsName}/{hashCode}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadTemplate(
            @PathVariable String templateName,
            @PathVariable String cmsName,
            @PathVariable long hashCode,
            Model model) {
        try {
            long delay = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - hashCode;
            if (delay < 0 || delay > 3600) {
                return returnTwigResponse("templates/error_expired.twig");
            }

            String filePath = "/pub/templates/" + cmsName + "/" + templateName + ".zip";
            logger.info("Downloading: " + filePath);
            return getResourceFileResponse(filePath,templateName + ".zip");
        }
        catch (IOException ex){
            logger.error("Caught IOException: " +  ex.getMessage());
            return returnTwigResponse("templates/error_404.twig");
        }
    }

    private ResponseEntity<InputStreamResource> returnTwigResponse(String twigFilename) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JtwigTemplate jtwigTemplate = new JtwigTemplate(
                    new ClasspathJtwigResource(twigFilename),
                    new JtwigConfiguration());
            jtwigTemplate.output(outputStream, new JtwigModelMap());
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType("text/html"))
                    .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
        }
        catch (Exception ex){
            logger.error("Cannot render twig template. Caught Exception: " +  ex.getMessage());
            return null;
        }
    }

    private ResponseEntity<InputStreamResource> getResourceFileResponse(String path, String fileName)
            throws IOException {

        ClassPathResource templateFile = new ClassPathResource(path);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        if( !fileName.isEmpty()){
            headers.add("content-disposition", "attachment; filename=" + fileName);
        }

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(templateFile.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(templateFile.getInputStream()));
    }

    @RequestMapping(method = POST)
    public String sendEmail(DownloadRequest downloadRequest) {
        downloadRequest.setTemplateName(template.getNamePl());
        downloadRequest.setCms(cms.getNamePl());
        downloadRequest.setCreated(LocalDateTime.now());
        logger.info("New download request: {}", downloadRequest);
        logger.info("Data: "+downloadRequest.getCreated().toString());

        downloadRequest = downloadRequestRepository.save(downloadRequest);

        long hashSend = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        HtmlEmailMessage htmlEmailMessage = new HtmlEmailMessage("noreply@pwd.dolinagubra.pl", downloadRequest.getAdministrativeEmail(),
                "Szablon CMS ze strony PWD",
                    getEmailMessageTemplate(),
                    getEmailMessageModelMap(template.getNamePl(), template.getDownloadName(), cms.getNamePl(), hashSend));

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

    private JtwigModelMap getEmailMessageModelMap(String name, String file, String cms, long hash) {
        return new JtwigModelMap()
                .withModelAttribute("name", name)
                .withModelAttribute("file", file)
                .withModelAttribute("cms", cms)
                .withModelAttribute("hash", hash)
                ;
    }
}
