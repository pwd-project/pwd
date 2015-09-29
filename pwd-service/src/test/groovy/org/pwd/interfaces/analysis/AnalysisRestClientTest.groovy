package org.pwd.interfaces.analysis

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.springframework.http.MediaType
import org.springframework.retry.support.RetryTemplate
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

/**
 * @author bartosz.walacik
 */
class AnalysisRestClientTest extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(8089)


    def "should integrate with pwd-analysis service"() {
        given:
        def analysisRestClient = new AnalysisRestClient('http://localhost:8089/analysis', new RetryTemplate())

        def expectedJson =
                """
        {
          "analysis": {
            "anyTitle": {
              "score": 100
            },
            "htmlLang": {
              "score": 0
            },
            "alt": {
              "score": null
            },
            "unknown-metric": {
              "score": null
            }
          },
          "status": {
            "responseCode": 200
          }
        }
        """

        wireMockRule.stubFor(get(urlPathEqualTo("/analysis"))
                .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
                .withBody(expectedJson)))

        when:
        def response = analysisRestClient.getAnalysis(new URL("http://allegro.pl")).get()

        then:
        response.metrics.size() == 3
        response.getMetric("anyTitle").value.get() == 100
        response.getMetric("htmlLang").value.get() == 0
        !response.getMetric("alt").value.isPresent()
        response.getHttpStatusCode() == 200
    }
}
