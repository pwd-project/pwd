package org.pwd.web.ajax;

import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/ajax")
@ResponseBody
class AjaxController {
    private WebsiteAuditRepository websiteAuditRepository;

    @Autowired
    public AjaxController(WebsiteAuditRepository websiteAuditRepository) {
        this.websiteAuditRepository = websiteAuditRepository;
    }

    @RequestMapping(value = "/average", method = GET)
    public String averageScore() {
        List<WebsiteAudit> auditList = websiteAuditRepository.findAll();
        double avg = auditList.stream()
                .mapToDouble(p -> p.getAuditReport().score())
                .average()
                .getAsDouble();
        return new DecimalFormat("#.00").format(avg);
    }
}
