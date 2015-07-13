package org.pwd.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bartosz.walacik
 */
@RestController
class WelcomeController {

    @RequestMapping("/")
    public Welcome welcome() {
        return new Welcome("Welcome to PWD service (KA).");
    }
}
