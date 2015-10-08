package org.pwd.web.pages;

import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public String pobierzPage() {
        return "pobierz";
    }

    @RequestMapping(value = "kontakt", method = GET)
    public String contactPage() {
        return "contact";
    }

    @RequestMapping(method = GET)
    public String indexPage(Model model){
        List<WebsiteAudit> auditList = websiteAuditRepository.getSorted();
        double averageScore = 0.0;
        for (WebsiteAudit audit : auditList) {
            averageScore += audit.getAuditReport().score();
        }
        averageScore /= auditList.size();

        model.addAttribute("averageScore", Math.round(averageScore*100)/100.0); // round to two decimal place
        return "index";
    }
}
