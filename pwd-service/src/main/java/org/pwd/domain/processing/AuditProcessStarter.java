package org.pwd.domain.processing;

import org.pwd.domain.audit.Audit;
import org.pwd.domain.audit.AuditRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bartosz.walacik
 */
@Service
public class AuditProcessStarter {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuditProcessStarter.class);

    private final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1));

    private final AuditProcess auditProcess;
    private AuditRepository auditRepository;

    @Autowired
    AuditProcessStarter(AuditProcess auditProcess, AuditRepository auditRepository) {
        this.auditProcess = auditProcess;
        this.auditRepository = auditRepository;
    }

    public int startAuditProcess() {
        Audit audit = new Audit().start();
        auditRepository.save(audit);

        executorService.submit(() -> {
            try {
                auditProcess.doAudit(audit);
            } catch (Exception e) {
                logger.error("audit request failed", e);
            }
        });

        logger.info("Audit request added to the queue");
        return audit.getId();
    }

}
