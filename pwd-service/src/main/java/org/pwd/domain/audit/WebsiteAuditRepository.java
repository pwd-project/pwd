package org.pwd.domain.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author bartosz.walacik
 */
public interface WebsiteAuditRepository extends JpaRepository<WebsiteAudit, Integer> {

    List<WebsiteAudit> findByAudit(Audit audit);

    WebsiteAudit findByAuditAndWebsiteId(Audit audit, int websiteId);

    List<WebsiteAudit> findByWebsiteId(int websiteId);

    @Query(nativeQuery = true, value =
            "SELECT w.id AS id, to_number(json_extract_path_text(audit_report,'score'),'999') as total_score, w.administrative_unit as name, w.url as url" +
                    "FROM   website_audit wa, website w" +
                    "WHERE  w.id = wa.website_fk" +
                    "AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
                    "ORDER BY TOTAL_SCORE desc" +
                    "LIMIT :maxRecords")
    List<WebsiteRank> getRanking(@Param("maxRecords") Integer maxRecords);
}
