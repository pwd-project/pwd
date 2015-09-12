package org.pwd.domain.audit;

import com.google.common.base.Preconditions;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.pwd.domain.websites.Website;
import org.pwd.hibernate.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author bartosz.walacik
 */
@Entity(name = "website_audit")
@Table(indexes = {@Index(name = "website_audit_website_idx", columnList = "website_fk"),
        @Index(name = "website_audit_audit_idx", columnList = "audit_fk")})
@TypeDefs({@TypeDef(name = "WebsiteAuditReportJson", typeClass = WebsiteAuditReportUserType.class)})
public class WebsiteAudit {
    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "website_audit_id_seq", name = "website_audit_id_seq")
    @GeneratedValue(generator = "website_audit_id_seq", strategy = GenerationType.SEQUENCE)
    private int id;

    @Type(type = "WebsiteAuditReportJson")
    private WebsiteAuditReport auditReport;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "website_fk")
    private Website website;

    @ManyToOne
    @JoinColumn(name = "audit_fk")
    private Audit audit;

    //only for Hibernate
    WebsiteAudit() {
    }

    WebsiteAudit(Website website, Audit audit, WebsiteAuditReport auditReport) {
        Preconditions.checkArgument(auditReport != null);
        Preconditions.checkArgument(website != null);
        Preconditions.checkArgument(audit != null);

        this.auditReport = auditReport;
        this.website = website;
        this.audit = audit;

        created = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public WebsiteAuditReport getAuditReport() {
        return auditReport;
    }

    public Website getWebsite() {
        return website;
    }

    public LocalDateTime getCreated() {
        return created;
    }

}
