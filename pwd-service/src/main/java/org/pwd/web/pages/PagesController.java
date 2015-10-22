package org.pwd.web.pages;

import org.pwd.domain.audit.WebsiteAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/")
class PagesController {
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

    @RequestMapping(value = "pobierz", method = GET)
    public String downloadPage() {
        return "download";
    }

    @RequestMapping(value = "kontakt", method = GET)
    public String contactPage() {
        return "contact";
    }
}
