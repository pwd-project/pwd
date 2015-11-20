package org.pwd.interfaces.analysis;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.tomcat.util.codec.binary.Base64;
import org.pwd.domain.audit.WebsiteAuditReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;


/**
 * @author bartosz.walacik
 */
@Service
public class AnalysisRestClient {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisRestClient.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final RetryTemplate retryTemplate;
    private final String analysisEndpointUrl;
    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AnalysisRestClient(@Value("${analysis.endpoint}") String analysisEndpointUrl, RetryTemplate retryTemplate) {
        Preconditions.checkArgument(!analysisEndpointUrl.isEmpty());
        this.retryTemplate = retryTemplate;
        this.analysisEndpointUrl = analysisEndpointUrl;
    }

    private void restartPwdAnalysis() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String auth = System.getenv().get("HEROKUAPI_AUTH");
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.add("Authorization", authHeader);
        headers.add("Accept", "application/vnd.heroku+json; version=3");
        HttpEntity<Object> request = new HttpEntity<>(headers);
        ResponseEntity<Void> exchange = restTemplate.exchange("https://api.heroku.com/apps/pwd-analysis/dynos", HttpMethod.DELETE, request, Void.class);
        if (exchange.getStatusCode() == HttpStatus.ACCEPTED) {
            logger.info("Restarted pwd analysis...");
        }
    }

    public Optional<WebsiteAuditReport> getAnalysis(URL websiteUrl) throws InterruptedException {
        int count = 0;
        int maxTries = 3;
        while (true) {
            try {
                String response = retryTemplate.execute(request -> restTemplate.getForObject(analysisEndpointUrl + "?url={websiteUrl}", String.class, websiteUrl));
                return Optional.of(new WebsiteAuditReport(gson.fromJson(response, JsonElement.class).getAsJsonObject()));
            } catch (Exception e) {
                restartPwdAnalysis();
                Thread.sleep(5000L);
                if (++count == maxTries) {
                    throw e;
                }
            }
        }
    }
}