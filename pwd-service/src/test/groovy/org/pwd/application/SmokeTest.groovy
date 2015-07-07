package org.pwd.application

import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification;

/**
 * @author bartosz.walacik
 */
@ActiveProfiles("integration")
@WebIntegrationTest
@SpringApplicationConfiguration(classes = PwdService)
class SmokeTest extends Specification {

    def "should run the App and connect to database"() {
        expect:
        true
    }

}
