package org.pwd.domain.audit;

import com.google.common.base.Preconditions;
import org.pwd.domain.websites.Website;
import org.pwd.hibernate.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.pwd.domain.audit.AuditProcessStatus.NEW;
import static org.pwd.domain.audit.AuditProcessStatus.STARTED;

/**
 * @author bartosz.walacik
 */
@Entity(name = "audit")
public class Audit {
    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "audit_id_seq", name = "audit_id_seq")
    @GeneratedValue(generator = "audit_id_seq", strategy = GenerationType.SEQUENCE)
    private int id;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime started;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime finished;

    @Enumerated(EnumType.STRING)
    private AuditProcessStatus processStatus;

    private int auditedSitesCount;

    public Audit() {
        this.processStatus = NEW;
    }

    public WebsiteAudit createWebsiteAudit(Website website, WebsiteAuditReport auditReport) {
        return new WebsiteAudit(website, this, auditReport);
    }

    public Audit start() {
        Preconditions.checkState(processStatus == NEW);
        started = LocalDateTime.now();
        processStatus = STARTED;
        return this;
    }

    public void done(int auditedSitesCount) {
        Preconditions.checkState(processStatus == STARTED);
        finished = LocalDateTime.now();
        processStatus = AuditProcessStatus.DONE;
        this.auditedSitesCount = auditedSitesCount;
    }

    public void broken(Exception e) {
        finished = LocalDateTime.now();
        processStatus = AuditProcessStatus.BROKEN;
    }

    public Duration duration() {
        if (started == null) {
            return Duration.ofSeconds(0);
        }
        if (finished == null) {
            return Duration.between(started, LocalDateTime.now());
        }
        return Duration.between(started, finished);
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

    public String durationAsString() {
        return duration().toMinutes() + " mi " + duration().getSeconds() % 60 + " s";
    }

    public int getAuditedSitesCount() {
        return auditedSitesCount;
    }
}
