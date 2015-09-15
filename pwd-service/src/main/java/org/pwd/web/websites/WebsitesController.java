package org.pwd.web.websites;

import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditRepository;
import org.pwd.domain.websites.Website;
import org.pwd.domain.websites.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author bartosz.walacik
 */
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
    public String getWebsites(Model model,
                              @RequestParam(value = "query", required = false) String query) {

        model.addAttribute("query", query);

        List<Website> websites;
        if (StringUtils.isEmpty(query)) {
            // Pusta lista
            websites = websiteRepository.search("null"); //websiteRepository.findAll();
        } else {
            websites = websiteRepository.search(query);
        }
        model.addAttribute("websites", websites);
        model.addAttribute("websitesTotalCount", websiteRepository.count());
        model.addAttribute("websitesCount", websites.size());

        return "websites";
    }

    @RequestMapping("/{websiteId}")
    public String getWebsiteAudits(Model model, @PathVariable("websiteId") Integer websiteId) {
        List<WebsiteAudit> websiteAudits = websiteAuditRepository.findByWebsiteId(websiteId);
        Collections.reverse(websiteAudits);
        model.addAttribute("websiteAudits", websiteAudits);
        return "websiteAudits";
    }
}
