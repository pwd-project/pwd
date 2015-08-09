package org.pwd.domain.audit

import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import org.pwd.PwdService
import org.pwd.domain.websites.Website
import org.pwd.domain.websites.WebsiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.annotation.Rollback
import spock.lang.Ignore
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

/**
 * @author bartosz.walacik
 */
@Ignore //requires Postgres on localhost

@WebIntegrationTest
@SpringApplicationConfiguration(classes = PwdService)
class WebsiteAuditRepositoryTest extends Specification {

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

        def website = websiteRepository.getOne(1)

        def report = new WebsiteAuditReport([new MetricValue("metricA", 0), new MetricValue("metricB", 100)])

        def websiteAudit = audit.createWebsiteAudit(website, report)

        when:
        websiteAudit = websiteAuditRepository.save(websiteAudit)
        def websiteAuditPersisted = reload(websiteAudit)

        then:
        websiteAudit != websiteAuditPersisted
        websiteAuditPersisted.auditReport instanceof WebsiteAuditReport
        with(websiteAuditPersisted.auditReport.metrics[0]){
            metricName == "metricA"
            value == 0
        }
        with(websiteAuditPersisted.auditReport.metrics[1]){
            metricName == "metricB"
            value == 100
        }
    }

    SessionImpl session(){
        (SessionImpl)entityManager.unwrap(Session)
    }

    WebsiteAudit reload(def websiteAudit){
        session().flush()
        session().evict(websiteAudit)
        websiteAuditRepository.getOne(websiteAudit.id)
    }
}
