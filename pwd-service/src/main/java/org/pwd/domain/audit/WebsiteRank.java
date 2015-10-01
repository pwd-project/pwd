package org.pwd.domain.audit;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author smikulsk
 */
public class WebsiteRank {
    private final Integer websiteId;
    private final Integer totalScore;
    private final String name;
    private final String url;


    WebsiteRank(Integer websiteId, Integer totalScore, String name, String url){
        checkArgument(websiteId != null);
        checkArgument(totalScore != null);
        checkArgument(name != null);
        checkArgument(url != null);

        this.websiteId = websiteId;
        this.totalScore = totalScore;
        this.name = name;
        this.url = url;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }
}
