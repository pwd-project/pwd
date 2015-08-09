package org.pwd.domain.audit;

import com.google.common.base.Preconditions;
import org.pwd.domain.websites.Website;
import org.pwd.hibernate.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.pwd.domain.audit.AuditProcessStatus.*;

/**
 * @author bartosz.walacik
 */
@Entity(name = "audit")
public class Audit {
    @Id
    @SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="audit_id_seq", name="audit_id_seq")
    @GeneratedValue(generator="audit_id_seq", strategy= GenerationType.SEQUENCE)
    private int id;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime started;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime finished;

    @Enumerated(EnumType.STRING)
    private AuditProcessStatus processStatus;

    public Audit() {
        this.processStatus = NEW;
    }

    public WebsiteAudit createWebsiteAudit(Website website, WebsiteAuditReport auditReport) {
        return new WebsiteAudit(website, this, auditReport);
    }

    public void start(){
        Preconditions.checkState(processStatus == NEW);
        started = LocalDateTime.now();
        processStatus = STARTED;
    }

    public void done(){
        Preconditions.checkState(processStatus == STARTED);
        finished = LocalDateTime.now();
        processStatus = AuditProcessStatus.DONE;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public AuditProcessStatus getProcessStatus() {
        return processStatus;
    }
}
