package org.pwd.domain.audit;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bartosz.walacik
 */
public interface WebsiteAuditRepository extends JpaRepository<WebsiteAudit, Integer> {
}
