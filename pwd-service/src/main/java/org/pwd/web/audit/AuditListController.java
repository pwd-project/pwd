package org.pwd.web.audit;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import org.pwd.domain.audit.*;
import org.pwd.domain.contact.HtmlEmailMessage;
import org.pwd.domain.processing.AuditProcessStarter;
import org.pwd.domain.websites.Website;
import org.pwd.interfaces.mailgun.MailgunClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author bartosz.walacik
 */
@Controller
@RequestMapping("/audyty")
class AuditListController {

    private static final Logger logger = LoggerFactory.getLogger(AuditListController.class);

    private final AuditProcessStarter auditProcessStarter;
    private final AuditRepository auditRepository;
    private final WebsiteAuditRepository websiteAuditRepository;
    private final MailgunClient mailgunClient;

    @Autowired
    public AuditListController(AuditProcessStarter auditProcessStarter, AuditRepository auditRepository,
                               WebsiteAuditRepository websiteAuditRepository, MailgunClient mailgunClient) {
        this.auditProcessStarter = auditProcessStarter;
        this.auditRepository = auditRepository;
        this.websiteAuditRepository = websiteAuditRepository;
        this.mailgunClient = mailgunClient;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showAudits() {
        return "audits";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String startAudit() {
        int auditId = auditProcessStarter.startAuditProcess();
        return "redirect:/audyty/" + auditId;
    }

    @RequestMapping(value="/email", method = RequestMethod.POST)
    public String sendEmails() {
        try {
            Optional<Audit> audit = auditRepository.findAll(new Sort(Sort.Direction.DESC, "id")).stream()
                    .filter(a -> a.getProcessStatus() == AuditProcessStatus.DONE)
                    .findFirst();
            if (audit.isPresent()) {
                logger.info("Trying to send email for audit {}", audit.get().getId());
                websiteAuditRepository.findByAudit(audit.get()).stream()
                        .filter(auditsWithScoreAndEmail())
                        .forEach(this::sendEmail);
            }
        }
        catch(Exception ex) {
            logger.error(ex.getMessage());
            return "error";
        }
        return "email_audits";
    }

    private Predicate<WebsiteAudit> auditsWithScoreAndEmail() {
        return  websiteAudit -> websiteAudit.getAuditScore() > 0 &&
                websiteAudit.getWebsite().getAdministrativeEmail() != null &&
                websiteAudit.getWebsite().getId() >= 2826;//ten warunek jest tymczasowy
    }

    private void sendEmail(WebsiteAudit websiteAudit) {
        HtmlEmailMessage htmlEmailMessage = new HtmlEmailMessage("noreply@pwd.dolinagubra.pl", websiteAudit.getWebsite().getAdministrativeEmail(),
                "Start projektu PWD", getEmailMessageTemplate(), getEmailMessageModelMap(websiteAudit.getAudit(), websiteAudit.getWebsite()));

        if (mailgunClient.sendEmail(htmlEmailMessage)) {
            logger.info("Email {} was sent successfully", htmlEmailMessage);
            return;
        }
        logger.warn("Email {} could not be sent", htmlEmailMessage);
        throw new RuntimeException("Could send email for audit " + websiteAudit.getId());
    }

    private JtwigTemplate getEmailMessageTemplate(){
        return new JtwigTemplate(
                new ClasspathJtwigResource("templates/emails/AuditEmail.twig"),
                new JtwigConfiguration()
        );
    }

    private JtwigModelMap getEmailMessageModelMap(Audit audit, Website website) {
        return new JtwigModelMap()
                .withModelAttribute("auditId", audit.getId())
                .withModelAttribute("auditFinished", audit.getFinished())
                .withModelAttribute("websiteUrl", website.getUrl())
                .withModelAttribute("websiteId", website.getId());
    }

    @ModelAttribute("audits")
    public List<Audit> loadAudits() {
        return auditRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }
}
