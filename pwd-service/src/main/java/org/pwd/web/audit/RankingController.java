package org.pwd.web.audit;

import org.pwd.domain.audit.WebsiteAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author smikulsk
 */
@Controller
@RequestMapping("/top-100")
public class RankingController {
    public static final int MAX_RECORDS = 100;
    private WebsiteAuditRepository websiteAuditRepository;

    @Autowired
    public RankingController(WebsiteAuditRepository websiteAuditRepository) {
        this.websiteAuditRepository = websiteAuditRepository;
    }

    @RequestMapping(method = GET)
    public String getWebsites(Model model) {
        model.addAttribute("ranking", websiteAuditRepository.getTop(MAX_RECORDS));
        return "top_100";
    }
}
