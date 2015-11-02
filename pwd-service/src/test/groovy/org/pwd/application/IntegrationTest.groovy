package org.pwd.application

import org.pwd.PwdService
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

/**
 * @author bartosz.walacik
 */
@ActiveProfiles("integration")
@WebIntegrationTest
@SpringApplicationConfiguration(classes = PwdService)
abstract class IntegrationTest extends Specification {
}
