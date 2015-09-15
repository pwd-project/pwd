package org.pwd.web.audit;

import org.pwd.domain.audit.Audit;
import org.pwd.domain.audit.AuditRepository;
import org.pwd.domain.processing.AuditProcessStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author bartosz.walacik
 */
@Controller
@RequestMapping("/audyty")
class AuditListController {

    private final AuditProcessStarter auditProcessStarter;
    private final AuditRepository auditRepository;

    @Autowired
    public AuditListController(AuditProcessStarter auditProcessStarter, AuditRepository auditRepository) {
        this.auditProcessStarter = auditProcessStarter;
        this.auditRepository = auditRepository;
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

    @ModelAttribute("audits")
    public List<Audit> loadAudits() {
        return auditRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }
}
