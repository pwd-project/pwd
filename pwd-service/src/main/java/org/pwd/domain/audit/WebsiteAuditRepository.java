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

    @Query(nativeQuery = true, value = "SELECT * " +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " ORDER BY to_number(json_extract_path_text(audit_report,'score'),'999') DESC" +
            " LIMIT :maxRecords")
    List<WebsiteAudit> getTop(@Param("maxRecords") Integer maxRecords);

    //@Query(nativeQuery = true, value = "SELECT -1 id,to_json('{httpStatusCode:200, score:' || avg(to_number(json_extract_path_text(audit_report,'score'),'999')) || ',metrics:[]}') audit_report, max(wa.created) created, -1 audit_fk, -1 website_fk " +
    //        " FROM   website_audit wa" +
    //        " WHERE  wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
    //        " LIMIT 1")
    //WebsiteAudit getAverageScore();

    @Query(nativeQuery = true, value = "SELECT * " +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " ORDER BY to_number(json_extract_path_text(audit_report,'score'),'999') DESC")
    List<WebsiteAudit> getSorted();

}