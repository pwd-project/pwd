package org.pwd.web.pages;

import org.pwd.domain.audit.WebsiteAuditRepository;
import org.pwd.domain.download.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/")
class PagesController {
    public static final int TOP_RECORDS = 5;
    private WebsiteAuditRepository websiteAuditRepository;

    @Autowired
    public PagesController(WebsiteAuditRepository websiteAuditRepository) {
        this.websiteAuditRepository = websiteAuditRepository;
    }

    @RequestMapping(value = "o-projekcie", method = GET)
    public String aboutPage() {
        return "about";
    }

    @RequestMapping(value = "aktualnosci", method = GET)
    public String blogPage() {
        return "blog";
    }

    @RequestMapping(value = "kontakt", method = GET)
    public String contactPage() {
        return "contact";
    }

    @RequestMapping(value = "regulamin", method = GET)
    public String regulaminPage() {
        return "terms";
    }

    @RequestMapping(method = GET)
    public String getWebsites(Model model) {
        model.addAttribute("template1", Template.valueOf("T11"));
        model.addAttribute("template2", Template.valueOf("T31"));
        model.addAttribute("template3", Template.valueOf("T61"));
        model.addAttribute("rankingTop", websiteAuditRepository.getTop(TOP_RECORDS));
        model.addAttribute("rankingTopChange", websiteAuditRepository.getTopChange(TOP_RECORDS));
        return "index";
    }

}
