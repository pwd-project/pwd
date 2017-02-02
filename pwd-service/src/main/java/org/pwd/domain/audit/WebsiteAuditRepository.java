package org.pwd.domain.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.ColumnResult;
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
            " AND    w.mc_site = 0" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " ORDER BY wa.audit_score DESC, w.id " +
            " LIMIT :maxRecords")
    List<WebsiteAudit> getTop(@Param("maxRecords") Integer maxRecords);

    @Query(nativeQuery = true, value =
            " SELECT wa.*, w.* " +
            " FROM   website w,  website_audit wa" +
            " WHERE  w.id = wa.website_fk" +
            " AND    w.mc_site = 0" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE')" +
            " AND    wa.audit_score > 0" +
            " AND    wa.audit_prev_score > 0" +
            " ORDER  BY wa.audit_score - wa.audit_prev_score DESC"+
            " LIMIT :maxRecords")
    List<WebsiteAudit> getTopChange(@Param("maxRecords") Integer maxRecords);

    @Query(nativeQuery = true, value =
            " SELECT wa.*" +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    w.mc_site = 0" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit WHERE process_status = 'DONE')" +
            " ORDER BY wa.audit_score DESC, w.id ")
    List<WebsiteAudit> getSorted();

    @Query(nativeQuery = true, value =
            " SELECT * " +
            " FROM   website_audit wa, website w" +
            " WHERE  w.id = wa.website_fk" +
            " AND    w.mc_site = 0" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit WHERE process_status = 'DONE')" +
            " AND    w.id = :websiteId")
    WebsiteAudit getCurrentScore(@Param("websiteId") int websiteId);

    @Query(nativeQuery = true, value =
            " SELECT NULLIF(MAX(wa.audit_score),0) " +
            " FROM   website_audit wa " +
            " WHERE  wa.website_fk = :websiteId" +
            " AND    wa.audit_fk = (SELECT MAX(id) FROM audit WHERE process_status = 'DONE' AND finished < current_date - interval '7 days' )")
    Double getPreviousScore(@Param("websiteId") int websiteId);

    /**
     * requires psql:
     * CREATE EXTENSION unaccent;
     */
    @Query(nativeQuery = true, value =
            " SELECT wa.*" +
            " FROM website w left join website_audit wa on w.id = wa.website_fk " +
            " WHERE wa.audit_fk = (SELECT MAX(id) FROM audit where process_status = 'DONE') " +
            " AND (" +
                    "(:searchPhrase = 'MC' AND w.mc_site = 1) " +
                    "OR " +
                    "(:searchPhrase = 'PIIT' AND w.mc_site = 2) " +
                    "OR " +
                    "(:searchPhrase <> 'MC' AND :searchPhrase <> 'PIIT' AND w.mc_site = 0)" +
                  ")" +
            " AND to_tsvector(unaccent( replace(w.url,'.', ' ') ||' '|| coalesce(w.city,'') ||' '|| coalesce(w.county,'') || ' ' || coalesce(w.voivodeship,'') || ' '|| coalesce(w.administrative_unit,'') || ' ' || coalesce(wa.cms_used,'') || ' ' || coalesce(w.unit_type,''))) " +
            " @@ to_tsquery(unaccent(replace(:searchPhrase,' ','&')))\n" +
            " ORDER BY wa.audit_score DESC, w.id ")
    List<WebsiteAudit> search(@Param("searchPhrase") String searchPhrase);
}
