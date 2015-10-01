package org.pwd.web.audit;

import org.pwd.domain.audit.WebsiteAuditRepository;
import org.pwd.domain.audit.WebsiteRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author smikulsk
 */
@Controller
@RequestMapping("/top-100")
public class RankingController {
    private WebsiteAuditRepository websiteAuditRepository;
    private final Integer maxRecords = 100;

    @Autowired
    public RankingController(WebsiteAuditRepository websiteAuditRepository) {
        this.websiteAuditRepository = websiteAuditRepository;
    }

    @RequestMapping(method = GET)
    public String getWebsites(Model model,
                              @RequestParam(value = "query", required = false) String query) {

        model.addAttribute("query", query);

        List<WebsiteRank> ranking;
        ranking = websiteAuditRepository.getRanking(maxRecords);

        model.addAttribute("ranking", ranking);

        return "top_100";
    }
}
