package org.pwd.interfaces.analysis;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.pwd.domain.audit.WebsiteAuditReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.time.Duration;


/**
 * @author bartosz.walacik
 */
@Service
public class AnalysisRestClient {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisRestClient.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final RetryTemplate retryTemplate = new RetryTemplate();
    private final String analysisEndpointUrl;
    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AnalysisRestClient(@Value("${analysis.endpoint}") String analysisEndpointUrl) {
        Preconditions.checkArgument(!analysisEndpointUrl.isEmpty());
        this.analysisEndpointUrl = analysisEndpointUrl;
        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(Duration.ofSeconds(15).toMillis());
        retryTemplate.setRetryPolicy(timeoutRetryPolicy);
    }

    public WebsiteAuditReport getAnalysis(URL websiteUrl) {
        String response = retryTemplate.execute(request -> restTemplate.getForObject(analysisEndpointUrl + "?url={websiteUrl}", String.class, websiteUrl));
        logger.info("analysis: \n" + response);
        JsonObject json = gson.fromJson(response, JsonElement.class).getAsJsonObject();
        return new WebsiteAuditReport(json);
    }

}