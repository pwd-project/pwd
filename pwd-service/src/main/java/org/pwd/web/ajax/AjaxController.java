package org.pwd.web.ajax;

import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public double averageScore() {
        List<WebsiteAudit> auditList = websiteAuditRepository.getSorted();
        double averageScore = 0.0;
        for (WebsiteAudit audit : auditList) {
            averageScore += audit.getAuditReport().score();
        }
        averageScore /= auditList.size();
        return Math.round(averageScore * 100) / 100.0;
    }
}
