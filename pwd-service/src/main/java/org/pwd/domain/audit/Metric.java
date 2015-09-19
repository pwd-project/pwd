package org.pwd.domain.audit;

import java.util.Optional;

/**
 * @author bartosz.walacik
 */
public enum Metric {
    alt(1, "1.1.1 Text alternatywny"),
    formattingTags(1, "1.3.1 Informacje i jej związki"),
    sound(1, "1.4.2 Kontrola dźwięku"),
    contrast(2, "1.4.3 Kontrast"),
    bigFont(2, "1.4.4 Zmiana rozmiaru tekstu"),
    anyTitle(1, "2.4.2 Tytuł strony"),
    headerContent(3, "2.4.10 Nagłówki sekcji"),
    htmlLang(1, "3.1.1 Język strony"),
    labels(1, "3.3.2 Etykiety lub instrukcje"),
    cms(0, "5.1.1 Używanie CMS"),
    contact(3, "5.1.2 Kontakt");

    private String namePl;
    private int weight;

    Metric(int weight, String namePl) {
        this.namePl = namePl;
        this.weight = weight;
    }

    public MetricValue create(int value) {
        return new MetricValue(this, value);
    }

    public MetricValue create(Optional<Integer> value) {
        return new MetricValue(this, value);
    }

    public static boolean exists(String metricName) {
        try {
            valueOf(metricName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getNamePl() {
        return namePl;
    }

    public int getWeight() {
        return weight;
    }

    public int getOrder() {
        return ordinal();
    }

}
