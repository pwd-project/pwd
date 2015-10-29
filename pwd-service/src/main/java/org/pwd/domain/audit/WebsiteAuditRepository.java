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

    @Query(nativeQuery = true, value = "SELECT wa.* " +
            " FROM   audit a, website_audit wa" +
            " WHERE  wa.website_fk = :websiteId" +
            " AND    a.id = wa.audit_fk " +
            " AND    a.process_status = 'DONE' " +
            " ORDER BY created")
    List<WebsiteAudit> findByWebsiteId(@Param("websiteId") int websiteId);

    @Query(nativeQuery = true, value =
            " SELECT wa.* " +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " ORDER BY wa.audit_score DESC" +
            " LIMIT :maxRecords")
    List<WebsiteAudit> getTop(@Param("maxRecords") Integer maxRecords);

    @Query(nativeQuery = true, value =
            " SELECT waa.*, w.*,  " +
            "        wap.audit_score as ScorePrev, " +
            "        waa.audit_score - wap.audit_score as score_change " +
            " FROM   website w,  website_audit waa,  website_audit wap" +
            " WHERE  w.id = waa.website_fk" +
            " AND    waa.audit_fk =" +
            " (" +
            " SELECT MAX(a1.id)" +
            " FROM   audit a1" +
            " WHERE  a1.process_status = 'DONE'" +
            " AND    a1.id = (SELECT MAX(a2.id) FROM audit a2 where a2.process_status = 'DONE')" +
            " )" +
            " AND    w.id = wap.website_fk" +
            " AND    wap.audit_fk = " +
            " (" +
            " SELECT MAX(a1.id)" +
            " FROM   audit a1" +
            " WHERE  a1.process_status = 'DONE'" +
            " AND    a1.finished < (SELECT MAX(a2.finished) - interval '7 days' FROM audit a2 where a2.process_status = 'DONE')" +
            " )" +
            " ORDER  BY waa.audit_score - wap.audit_score DESC"+
            " LIMIT :maxRecords")
    List<WebsiteAudit> getTopChange(@Param("maxRecords") Integer maxRecords);

    @Query(nativeQuery = true, value =
            " SELECT wa.*" +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " ORDER BY wa.audit_score DESC, w.id ")
    List<WebsiteAudit> getSorted();

    @Query(nativeQuery = true, value =
            " SELECT * " +
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
            " AND to_tsvector(unaccent( replace(w.url,'.', ' ') ||' '|| coalesce(w.city,'') ||' '|| coalesce(w.county,'') || ' ' || coalesce(w.voivodeship,'') || ' '|| coalesce(w.administrative_unit,'') || ' ' || coalesce(wa.cms_used,''))) " +
            " @@ to_tsquery(unaccent(replace(:searchPhrase,' ','&')))\n" +
            " ORDER BY wa.audit_score DESC, w.id ")
    List<WebsiteAudit> search(@Param("searchPhrase") String searchPhrase);
}
