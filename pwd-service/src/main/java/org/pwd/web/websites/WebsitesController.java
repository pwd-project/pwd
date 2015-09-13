package org.pwd.web.websites;

import org.pwd.domain.websites.Website;
import org.pwd.domain.websites.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author bartosz.walacik
 */
@Controller
@RequestMapping("/serwisy")
class WebsitesController {

    private WebsiteRepository websiteRepository;

    @Autowired
    public WebsitesController(WebsiteRepository websiteRepository) {
        this.websiteRepository = websiteRepository;
    }

    @RequestMapping(method = GET)
    public String getWebsites(Model model,
                              @RequestParam(value = "query", required = false) String query) {

        model.addAttribute("query", query);

        List<Website> websites;
        if (StringUtils.isEmpty(query)) {
            websites = websiteRepository.search("XYZ"); //websiteRepository.findAll();
        } else {
            websites = websiteRepository.search(query);
        }
        model.addAttribute("websites", websites);
        model.addAttribute("websitesTotalCount", websiteRepository.count());
        model.addAttribute("websitesCount", websites.size());

        return "websites";
    }
}
