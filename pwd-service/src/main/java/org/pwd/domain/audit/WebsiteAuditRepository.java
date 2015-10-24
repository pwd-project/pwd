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

    @Query(nativeQuery = true, value = "SELECT * " +
            " FROM   website_audit wa" +
            " WHERE  wa.website_fk = :websiteId" +
            " ORDER BY created")
    List<WebsiteAudit> findByWebsiteId(@Param("websiteId") int websiteId);

    @Query(nativeQuery = true, value = "SELECT wa.* " +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " ORDER BY to_number(json_extract_path_text(audit_report,'score'),'999') DESC" +
            " LIMIT :maxRecords")
    List<WebsiteAudit> getTop(@Param("maxRecords") Integer maxRecords);

    @Query(nativeQuery = true, value = "SELECT wa.*" +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " ORDER BY to_number(json_extract_path_text(audit_report,'score'),'999') DESC")
    List<WebsiteAudit> getSorted();

    @Query(nativeQuery = true, value = "SELECT * " +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " AND    w.id = :websiteId")
    WebsiteAudit getCurrentScore(@Param("websiteId") int websiteId);

    /**
     * requires psql:
     * CREATE EXTENSION unaccent;
     */
    @Query(nativeQuery = true, value =
            " SELECT wa.*" +
                    " FROM website w left join website_audit wa on w.id = wa.website_fk " +
                    " WHERE wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE') " +
                    " AND to_tsvector(unaccent( replace(w.url,'.', ' ') ||' '|| coalesce(w.city,'') ||' '|| coalesce(w.county,'') || ' ' || coalesce(w.voivodeship,'') || ' '|| coalesce(w.administrative_unit,''))) " +
                    " @@ to_tsquery(unaccent(replace(:searchPhrase,' ','&')))\n" +
                    " ORDER BY to_number(json_extract_path_text(audit_report,'score'),'999.99') DESC")
    List<WebsiteAudit> search(@Param("searchPhrase") String searchPhrase);
}