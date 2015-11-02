package org.pwd.domain.download

import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import org.pwd.application.IntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

/**
 * @author kadamowi
 */
class DownloadRequestRepositoryTest extends IntegrationTest {

    @Autowired
    DownloadRequestRepository downloadRequestRepository

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Rollback(false)
    def "should persist DownloadRequest in database"() {
        given:
        def DownloadRequest downloadRequest = new DownloadRequest("T1","WordPress","Gmina","Warszawa","email")

        when:
        downloadRequest = downloadRequestRepository.save(downloadRequest)
        def downloadRequestPersisted = reload(downloadRequest)

        then:
        downloadRequestPersisted != downloadRequest
        downloadRequestPersisted instanceof DownloadRequest
        downloadRequestPersisted.templateName == "T1"
        downloadRequestPersisted.cms == "WordPress"
        downloadRequestPersisted.unitName == "Gmina"
        downloadRequestPersisted.city == "Warszawa"
        downloadRequestPersisted.administrativeEmail == "email"
    }

    SessionImpl session() {
        (SessionImpl) entityManager.unwrap(Session)
    }

    DownloadRequest reload(downloadRequest){
        session().flush()
        session().evict(downloadRequest)
        downloadRequestRepository.getOne(downloadRequest.id)
    }
}
