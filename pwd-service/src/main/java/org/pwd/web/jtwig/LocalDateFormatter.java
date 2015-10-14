package org.pwd.web.jtwig;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateFormatter {
    @JtwigFunction(name = "localDateTime")
    public String localDateTime(@Parameter LocalDateTime inputDate) {
        return inputDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @JtwigFunction(name = "localDate")
    public String localDate(@Parameter LocalDateTime inputDate) {
        return inputDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}