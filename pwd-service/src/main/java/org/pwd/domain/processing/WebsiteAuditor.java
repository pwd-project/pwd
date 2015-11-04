package org.pwd.domain.processing;

import org.pwd.domain.audit.Audit;
import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditReport;
import org.pwd.domain.audit.WebsiteAuditRepository;
import org.pwd.domain.websites.Website;
import org.pwd.interfaces.analysis.AnalysisRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Does audit for one website
 *
 * @author bartosz.walacik
 */
@Service
class WebsiteAuditor {
    private final AnalysisRestClient analysisRestClient;
    private final WebsiteAuditRepository websiteAuditRepository;

    @Autowired
    WebsiteAuditor(AnalysisRestClient analysisRestClient, WebsiteAuditRepository websiteAuditRepository) {
        this.analysisRestClient = analysisRestClient;
        this.websiteAuditRepository = websiteAuditRepository;
    }

    WebsiteAudit auditWebsite(Audit audit, Website website) {

        Optional<WebsiteAuditReport> auditReport = analysisRestClient.getAnalysis(website.getUrl());
        WebsiteAudit websiteAudit = auditReport
                .map(websiteAuditReport -> audit.createWebsiteAudit(website, websiteAuditReport,websiteAuditRepository.getPreviousScore(website.getId())))
                .orElseThrow(AnalysisNotCompleteException::new);


        return websiteAuditRepository.save(websiteAudit);
    }
}
