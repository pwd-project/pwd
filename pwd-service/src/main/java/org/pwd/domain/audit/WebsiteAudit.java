package org.pwd.domain.audit;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.pwd.hibernate.PostgresJsonUserType;

import javax.persistence.*;

/**
 * @author bartosz.walacik
 */
@Entity(name = "website_audit")
@TypeDefs({ @TypeDef(name = "JsonObject", typeClass = PostgresJsonUserType.class) })
public class WebsiteAudit {
    @Id
    @SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="website_audit_id_seq", name="website_audit_id_seq")
    @GeneratedValue(generator="website_audit_id_seq", strategy= GenerationType.SEQUENCE)
    private int id;

    @Type(type = "JsonObject")
    private WebsiteAuditReport auditReport;

    public WebsiteAudit(WebsiteAuditReport auditReport) {
        this.auditReport = auditReport;
    }

    public int getId() {
        return id;
    }

    public WebsiteAuditReport getAuditReport() {
        return auditReport;
    }
}
