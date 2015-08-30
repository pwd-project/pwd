package org.pwd.domain.audit

import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import org.pwd.application.IntegrationTest
import org.pwd.domain.websites.Website
import org.pwd.domain.websites.WebsiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

import static org.pwd.domain.audit.Metric.alt
import static org.pwd.domain.audit.Metric.anyTitle
import static org.pwd.domain.audit.Metric.sound

/**
 * @author bartosz.walacik
 */
class WebsiteAuditRepositoryTest extends IntegrationTest {

    @Autowired
    AuditRepository auditRepository

    @Autowired
    WebsiteRepository websiteRepository

    @Autowired
    WebsiteAuditRepository websiteAuditRepository

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Rollback(false)
    def "should persist WebsiteAudit with WebsiteAuditReport as Postgres JSON document"(){
        given:
        def audit = new Audit()
        audit.start()
        audit = auditRepository.save(audit)

        def website = getOrCreateWebsite()
        def report = new WebsiteAuditReport(200, [ alt.create(0), anyTitle.create(100), sound.create(100)])
        def websiteAudit = audit.createWebsiteAudit(website, report)

        when:
        websiteAudit = websiteAuditRepository.save(websiteAudit)
        def websiteAuditPersisted = reload(websiteAudit)

        then:
        websiteAudit != websiteAuditPersisted
        websiteAuditPersisted.auditReport instanceof WebsiteAuditReport
        websiteAuditPersisted.auditReport.httpStatusCode == 200
        with(websiteAuditPersisted.auditReport.metrics[0]){
            metric == alt
            value.get() == 0
        }
        with(websiteAuditPersisted.auditReport.metrics[1]){
            metric == sound
            value.get() == 100
        }
        with(websiteAuditPersisted.auditReport.metrics[2]){
            metric == anyTitle
            value.get() == 100
        }
    }

    SessionImpl session(){
        (SessionImpl)entityManager.unwrap(Session)
    }

    Website getOrCreateWebsite(){
        if (!websiteRepository.exists(1)){
            websiteRepository.save(new Website(1, new URL("http://example.com/")))
        }
        websiteRepository.getOne(1)
    }

    WebsiteAudit reload(def websiteAudit){
        session().flush()
        session().evict(websiteAudit)
        websiteAuditRepository.getOne(websiteAudit.id)
    }
}
