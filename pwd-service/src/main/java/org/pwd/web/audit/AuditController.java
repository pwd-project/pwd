package org.pwd.web.audit;

import org.pwd.domain.audit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bartosz.walacik
 */
@Controller
@RequestMapping("/audyty")
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

        model.addAttribute("metrics", Arrays.asList(Metric.values()));
        model.addAttribute("audit", audit);
        model.addAttribute("websiteAudits", websiteAudits);

        return "audit";
    }

    @RequestMapping(value = "/{auditId}.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<WebsiteAuditResponse> showAuditAsJson(@PathVariable int auditId, HttpServletResponse response) {
        Audit audit = auditRepository.findOne(auditId);
        List<WebsiteAudit> websiteAudits = websiteAuditRepository.findByAudit(audit);
        response.setHeader("Content-Disposition", "attachment;filename=audit_" + auditId + "_" + System.currentTimeMillis() + ".json");
        return websiteAudits.stream().map(WebsiteAuditResponse::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{auditId}/{websiteId}", method = RequestMethod.GET)
    public String showAuditedWebsite(@PathVariable int auditId, @PathVariable int websiteId, Model model) {
        Audit audit = auditRepository.findOne(auditId);
        WebsiteAudit websiteAudit = websiteAuditRepository.findByAuditAndWebsiteId(audit, websiteId);

        model.addAttribute("metrics", Arrays.asList(Metric.values()));
        model.addAttribute("audit", audit);
        model.addAttribute("websiteAudit", websiteAudit);

        return "website";
    }


}