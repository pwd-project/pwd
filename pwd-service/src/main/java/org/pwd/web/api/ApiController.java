package org.pwd.web.api;

import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditRepository;
import org.pwd.domain.websites.Website;
import org.pwd.domain.websites.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api")
class ApiController {
    private WebsiteAuditRepository websiteAuditRepository;
    private WebsiteRepository websiteRepository;

    @Autowired
    public ApiController(WebsiteAuditRepository websiteAuditRepository, WebsiteRepository websiteRepository) {
        this.websiteAuditRepository = websiteAuditRepository;
        this.websiteRepository = websiteRepository;
    }

    @RequestMapping(value = "/gwd", method = GET)
    public ApiResponse globalAverageScore() {
        List<WebsiteAudit> auditList = websiteAuditRepository.getSorted();
        double avg = auditList.stream()
                .mapToDouble(p -> p.getAuditReport().score())
                .average()
                .getAsDouble();
        return new ApiResponse(avg);
    }

    @RequestMapping(value = "/gwd/{domain}", method = GET)
    public ResponseEntity<ApiResponse> websiteAverageScore(@PathVariable String domain) {
        URL url;
        try {
            url = new URL("http://" + domain + "/");
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Website website = websiteRepository.findOneByUrl(url);
        if (website == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        WebsiteAudit currentWebsiteAudit = websiteAuditRepository.getCurrentScore(website.getId());
        double auditScore = currentWebsiteAudit.getAuditScore();
        return new ResponseEntity<>(new ApiResponse(auditScore), HttpStatus.OK);
    }
}
