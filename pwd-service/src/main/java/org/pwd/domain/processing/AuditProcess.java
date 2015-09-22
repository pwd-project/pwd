package org.pwd.domain.processing;

import org.pwd.domain.audit.Audit;
import org.pwd.domain.audit.AuditRepository;
import org.pwd.domain.websites.Website;
import org.pwd.domain.websites.WebsiteRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bartosz.walacik
 */
@Service
class AuditProcess {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuditProcess.class);

    private final AuditRepository auditRepository;
    private final WebsiteRepository websiteRepository;
    private final WebsiteAuditor websiteAuditor;

    @Autowired
    AuditProcess(AuditRepository auditRepository, WebsiteRepository websiteRepository, WebsiteAuditor websiteAuditor) {
        this.auditRepository = auditRepository;
        this.websiteRepository = websiteRepository;
        this.websiteAuditor = websiteAuditor;
    }

    void doAudit(Audit audit) {

        logger.info("Starting audit#{}  process ...", audit.getId());

        List<Website> websites = websiteRepository.findAll(new Sort(Sort.Direction.ASC, "id"));

        try {
            auditWebsites(audit, websites);
            audit.done();
        } catch (Exception e) {
            audit.broken();
            logger.error("Audit process broken by exception", e);
        }

        auditRepository.save(audit);

        logger.info("Finished audit#{}, time elapsed: {}, websites audited: {}",
                audit.getId(), audit.durationAsString(), websites.size());
    }

    private void auditWebsites(Audit audit, List<Website> websites) {
        long start = System.currentTimeMillis();
        int i = 0;
        for (Website website : websites) {
            i++;

            long pageStart = System.currentTimeMillis();

            logger.info(i + ". auditing website: {}", website.getUrl());

            try {
                websiteAuditor.auditWebsite(audit, website);
                long pageStop = System.currentTimeMillis();

                long pageAverage = (pageStop - start) / i;
                logger.info(" .. website audited in {} millis, website average: {} millis, {}% of all websites processed", (pageStop - pageStart), pageAverage, (i * 100 / websites.size()));

                audit.mark();
                auditRepository.save(audit);
            } catch (AnalysisNotCompleteException e) {
                logger.error("Could not finish analysis of {}", website.getUrl());
            }
        }
    }

}
