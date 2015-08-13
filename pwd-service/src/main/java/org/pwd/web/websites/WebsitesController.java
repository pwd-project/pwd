package org.pwd.web.websites;

import org.pwd.domain.websites.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author bartosz.walacik
 */
@Controller
@RequestMapping("/websites")
class WebsitesController {

    private WebsiteRepository websiteRepository;

    @Autowired
    public WebsitesController(WebsiteRepository websiteRepository) {
        this.websiteRepository = websiteRepository;
    }

    @RequestMapping(method= GET)
    public String getWebsites(Model model) {

        model.addAttribute("websites", websiteRepository.findAll());

        return "websites";
    }
}
