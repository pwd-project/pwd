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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private HashMap<Integer,Integer> placesMap;

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
        ArrayList<Integer> currentScores = new ArrayList<Integer>();
        ArrayList<Integer> currentPlaces = new ArrayList<Integer>();
        if (StringUtils.isEmpty(query)) {
            websites = Collections.emptyList();
        } else {
            initPlacesMap();
            websites = websiteRepository.search(query);
            for(Website website : websites){
                currentScores.add(getCurrentScore(website.getId()));
                currentPlaces.add(getCurrentPlace(website.getId()));
            }
        }
        model.addAttribute("websites", websites);
        model.addAttribute("websitesTotalCount", websiteRepository.count());
        model.addAttribute("websitesCount", websites.size());
        model.addAttribute("currentScores", currentScores);
        model.addAttribute("currentPlaces", currentPlaces);

        return "websites";
    }

    @RequestMapping("/{websiteId}")
    public String getWebsiteAudits(Model model, @PathVariable("websiteId") Integer websiteId) {
        List<WebsiteAudit> websiteAudits = websiteAuditRepository.findByWebsiteId(websiteId);
        Website website = websiteRepository.findOne(websiteId);
        Collections.reverse(websiteAudits);
        model.addAttribute("websiteAudits", websiteAudits);
        model.addAttribute("website", website);
        model.addAttribute("currentScore",getCurrentScore(websiteId));
        model.addAttribute("currentPlace",getCurrentPlace(websiteId));
        return "websiteAudits";
    }

    private int getCurrentScore(int websiteId) {
        WebsiteAudit lastAudit = websiteAuditRepository.getCurrentScore(websiteId);
        if (lastAudit == null)
            return 0;
        else
            return lastAudit.getAuditReport().score();
    }

    private int getCurrentPlace(int websiteId) {
        if( placesMap == null ) initPlacesMap();
        return placesMap.getOrDefault(websiteId,placesMap.size());
    }

    private void initPlacesMap() {
        placesMap = new HashMap<Integer, Integer>();
        List<WebsiteAudit> sortedWebsiteAudits = websiteAuditRepository.getSorted();
        int place = 1;
        for(WebsiteAudit websiteAudit : sortedWebsiteAudits){
            placesMap.put(websiteAudit.getWebsite().getId(),place);
            ++place;
        }
    }
}
