package org.pwd.domain.contact

import org.hibernate.Session
import org.hibernate.internal.SessionImpl
import org.pwd.application.IntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

/**
 * @author Sławomir Mikulski
 */
class ContactRequestRepositoryTest extends IntegrationTest {

    @Autowired
    ContactRequestRepository contactRequestRepository

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Rollback(false)
    def "should persist ContactRequest in database"() {
        given:
        def ContactRequest contactRequest = new ContactRequest("dummyName","dummyEmail","dummyMobile","dummySite","dummyMessage")

        when:
        contactRequest = contactRequestRepository.save(contactRequest)
        def contactRequestPersisted = reload(contactRequest)

        then:
        contactRequestPersisted != contactRequest
        contactRequestPersisted instanceof ContactRequest
        contactRequestPersisted.name == "dummyName"
        contactRequestPersisted.administrativeEmail == "dummyEmail"
        contactRequestPersisted.mobile == "dummyMobile"
        contactRequestPersisted.site == "dummySite"
        contactRequestPersisted.message == "dummyMessage"
    }

    SessionImpl session() {
        (SessionImpl) entityManager.unwrap(Session)
    }

    ContactRequest reload(contactRequest){
        session().flush()
        session().evict(contactRequest)
        contactRequestRepository.getOne(contactRequest.id)
    }
}
