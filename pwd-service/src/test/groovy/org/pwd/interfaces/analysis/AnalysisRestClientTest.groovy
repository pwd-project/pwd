package org.pwd.interfaces.analysis

import org.pwd.application.IntegrationTest
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author bartosz.walacik
 */
class AnalysisRestClientTest extends IntegrationTest {

    @Autowired AnalysisRestClient analysisRestClient

    def "should integrate with pwd-analysis service"(){

      when:
      def response = analysisRestClient.getAnalysis(new URL("http://allegro.pl"))

      then:
      response.getMetric("anyTitle").value == 100
    }
}
