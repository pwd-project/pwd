package org.pwd.web.download

import org.pwd.application.IntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author smikulsk
 */
class DownloadTemplateTest  extends IntegrationTest {

    @Autowired
    DownloadRequestParamsHasher downloadRequestParamsHasher;

    def "should download template for proper hashcode"() {
        given:
        def testRestTemplate = new TestRestTemplate();
        def timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        def hashSend = downloadRequestParamsHasher.getHash('T11','WORDPRESS',timestamp);

        when:
        ResponseEntity response = testRestTemplate.getForEntity('http://localhost:8081/pobierz/{template}/{cms}/{hash}/{timestamp}', ResponseEntity.class, 'T11', 'WORDPRESS', hashSend, timestamp)

        then:
        response.statusCode == HttpStatus.OK;
        with(response.headers){
            contentType == MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    def "should not download template for expired hashcode"() {
        given:
        def testRestTemplate = new TestRestTemplate();
        def timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)-4000;
        def hashSend = downloadRequestParamsHasher.getHash('T11','WORDPRESS',timestamp);

        when:
        def response = testRestTemplate.getForObject('http://localhost:8081/pobierz/{template}/{cms}/{hash}/{timestamp}', String.class, 'T11', 'WORDPRESS', hashSend, timestamp);

        then:
        response.contains("Przepraszamy link już nie jest aktywny")
    }

    def "should not download template for bad template"() {
        given:
        def testRestTemplate = new TestRestTemplate();
        def timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        def hashSend = downloadRequestParamsHasher.getHash('bad_template','WORDPRESS',timestamp);

        when:
        def response = testRestTemplate.getForObject('http://localhost:8081/pobierz/{template}/{cms}/{hash}/{timestamp}', String.class, 'bad_template', 'WORDPRESS', hashSend, timestamp);

        then:
        response.contains("nic tu nie ma")
    }

    def "should not download template for bad cms"() {
        given:
        def testRestTemplate = new TestRestTemplate();
        def timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        def hashSend = downloadRequestParamsHasher.getHash('T11','bad_cms',timestamp);

        when:
        def response = testRestTemplate.getForObject('http://localhost:8081/pobierz/{template}/{cms}/{hash}/{timestamp}', String.class, 'T11', 'bad_cms', hashSend, timestamp);

        then:
        response.contains("nic tu nie ma");
    }
}
