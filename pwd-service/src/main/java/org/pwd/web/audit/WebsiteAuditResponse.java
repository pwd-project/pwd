package org.pwd.web.audit;

import org.pwd.domain.audit.WebsiteAudit;
import org.pwd.domain.audit.WebsiteAuditReport;
import org.pwd.domain.websites.Website;

public class WebsiteAuditResponse {
    private int id;
    private WebsiteAuditReport auditReport;
    private String created;
    private Website website;

    public WebsiteAuditResponse(WebsiteAudit websiteAudit) {
        this.id = websiteAudit.getId();
        this.auditReport = websiteAudit.getAuditReport();
        this.created = websiteAudit.getCreated().toString();
        this.website = websiteAudit.getWebsite();
    }

    public int getId() {
        return id;
    }

    public WebsiteAuditReport getAuditReport() {
        return auditReport;
    }

    public String getCreated() {
        return created;
    }

    public Website getWebsite() {
        return website;
    }
}
