package org.pwd.domain.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author bartosz.walacik
 */
public interface WebsiteAuditRepository extends JpaRepository<WebsiteAudit, Integer> {

    List<WebsiteAudit> findByAudit(Audit audit);

    WebsiteAudit findByAuditAndWebsiteId(Audit audit, int websiteId);

    List<WebsiteAudit> findByWebsiteId(int websiteId);
}
