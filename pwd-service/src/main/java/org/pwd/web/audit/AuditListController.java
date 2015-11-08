package org.pwd.web.audit;

import org.pwd.domain.audit.*;
import org.pwd.domain.contact.EmailMessage;
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
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
                        .forEach(websiteAudit1 -> sendEmail(websiteAudit1));
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
        EmailMessage emailMessage = new EmailMessage("noreply@pwd.dolinagubra.pl", websiteAudit.getWebsite().getAdministrativeEmail(),
                "Wyniki audytu ze strony PWD", composeMessage(websiteAudit.getAudit(), websiteAudit.getWebsite()), true);

        if (mailgunClient.sendEmail(emailMessage)) {
            logger.info("Email {} was sent successfully", emailMessage);
            return;
        }
        logger.warn("Email {} could not be sent", emailMessage);
        throw new RuntimeException("Could send email for audit " + websiteAudit.getId());
    }

    private String composeMessage(Audit audit, Website website) {
        String email_text = getFile("static/pages/AuditEmailTemplate.html");
        return String.format(email_text, audit.getId(),
                website.getUrl(),audit.getId(), website.getId());
    }

    private String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        File file = getFileFromResourceFolder(fileName);

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private File getFileFromResourceFolder(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }

    @ModelAttribute("audits")
    public List<Audit> loadAudits() {
        return auditRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }
}
