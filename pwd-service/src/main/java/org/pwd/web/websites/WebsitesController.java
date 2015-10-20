package org.pwd.web.websites;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditRepository;
import org.pwd.domain.websites.Website;
import org.pwd.domain.websites.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/serwisy")
class WebsitesController {

    private WebsiteRepository websiteRepository;
    private WebsiteAuditRepository websiteAuditRepository;

    @Autowired
    public WebsitesController(WebsiteRepository websiteRepository, WebsiteAuditRepository websiteAuditRepository) {
        this.websiteRepository = websiteRepository;
        this.websiteAuditRepository = websiteAuditRepository;
    }

    @RequestMapping(method = GET)
    public String getWebsites(Model model, @RequestParam(value = "query", required = false) String query) {
        List<WebsiteAudit> websitesAudits;
        if (StringUtils.isEmpty(query)) {
            websitesAudits = Collections.emptyList();
        } else {
            websitesAudits = websiteAuditRepository.search(StringEscapeUtils.escapeJava(query));
        }
        model.addAttribute("query", query);
        model.addAttribute("websitesAudits", websitesAudits);
        model.addAttribute("websitesTotalCount", websiteRepository.count());
        model.addAttribute("websitesCount", websitesAudits.size());

        return "websites";
    }

    @RequestMapping("/{websiteId}")
    public String getWebsiteAudits(Model model, @PathVariable("websiteId") Integer websiteId) {
        List<WebsiteAudit> websiteAudits = websiteAuditRepository.findByWebsiteId(websiteId);
        Website website = websiteRepository.findOne(websiteId);
        Collections.reverse(websiteAudits);
        model.addAttribute("websiteAudits", websiteAudits);
        model.addAttribute("website", website);

        return "websiteAudits";
    }
}
