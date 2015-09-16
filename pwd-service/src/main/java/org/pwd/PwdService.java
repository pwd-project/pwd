package org.pwd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author bartosz.walacik
 */
@SpringBootApplication
public class PwdService {
    public static void main(String[] args) {
        SpringApplication.run(PwdService.class, args);
    }
}
