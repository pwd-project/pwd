package org.pwd.web.audit;

import org.pwd.domain.audit.Audit;
import org.pwd.domain.audit.AuditRepository;
import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author bartosz.walacik
 */
@Controller
@RequestMapping("/audits")
public class AuditController {

    private final AuditRepository auditRepository;
    private final WebsiteAuditRepository websiteAuditRepository;

    @Autowired
    public AuditController(AuditRepository auditRepository, WebsiteAuditRepository websiteAuditRepository) {
        this.auditRepository = auditRepository;
        this.websiteAuditRepository = websiteAuditRepository;
    }

    @RequestMapping(value = "/{auditId}", method = RequestMethod.GET)
    public String showAudit(@PathVariable int auditId, Model model) {
        Audit audit = auditRepository.findOne(auditId);
        List<WebsiteAudit> websiteAudits = websiteAuditRepository.findByAudit(audit);

        model.addAttribute("audit", audit);
        model.addAttribute("websiteAudits", websiteAudits);

        return "/audit";
    }
}