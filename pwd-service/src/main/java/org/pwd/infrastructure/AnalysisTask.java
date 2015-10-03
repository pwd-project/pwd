package org.pwd.infrastructure;

import org.pwd.domain.processing.AuditProcessStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalysisTask {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisTask.class);
    private final AuditProcessStarter auditProcessStarter;

    @Autowired
    public AnalysisTask(AuditProcessStarter auditProcessStarter) {
        this.auditProcessStarter = auditProcessStarter;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void automatedAudit() {
        logger.info("Started automatic audit process at " + LocalDateTime.now());
        auditProcessStarter.startAuditProcess();
    }
}
