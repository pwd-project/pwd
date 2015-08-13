package org.pwd.domain.processing;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @author bartosz.walacik
 */
@Service
public class AuditProcessStarter {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuditProcessStarter.class);


    //Single thread pool, jobs queue with capacity 1
    private final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1));

    private final AuditProcess auditProcess;

    @Autowired
    AuditProcessStarter(AuditProcess auditProcess) {
        this.auditProcess = auditProcess;
    }

    public void startAuditProcess(){

        executorService.submit( () ->
        {
            try {
                auditProcess.doAudit();
            } catch(Exception e){
                logger.error("audit request failed", e);
            }
        }
        );

        logger.info("Audit request added to the queue");
    }

}
