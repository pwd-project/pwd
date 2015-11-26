package org.pwd.web.download;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

/**
 * @author smikulsk
 */
@Component
public class DownloadRequestParamsHasher {
    private final HashFunction hasher = Hashing.md5();

    public String getHash(String templateName, String cms, long timestamp) {
        return hasher
                .newHasher()
                .putString(templateName, Charsets.UTF_8)
                .putString(cms, Charsets.UTF_8)
                .putLong(timestamp)
                .hash()
                .toString();
    }
}
