package org.pwd.domain.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author bartosz.walacik
 */
public interface AuditRepository extends JpaRepository<Audit, Integer> {
}
