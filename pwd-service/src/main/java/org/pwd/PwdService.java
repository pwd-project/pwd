package org.pwd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author bartosz.walacik
 */
@SpringBootApplication
@EnableScheduling
public class PwdService {
    public static void main(String[] args) {
        SpringApplication.run(PwdService.class, args);
    }
}
