package org.pwd.web.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/")
class PagesController {
    @RequestMapping(value = "o-projekcie", method = GET)
    public String aboutPage() {
        return "about";
    }

    @RequestMapping(value = "aktualnosci", method = GET)
    public String blogPage() {
        return "blog";
    }

    @RequestMapping(value = "kontakt", method = GET)
    public String contactPage() {
        return "contact";
    }
}
