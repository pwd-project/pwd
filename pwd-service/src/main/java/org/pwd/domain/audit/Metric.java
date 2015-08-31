package org.pwd.domain.audit;

import java.util.Optional;

/**
 * @author bartosz.walacik
 */
public enum Metric {
    alt      (1, "1.1.1 Text alternatywny"),
    sound    (1, "1.4.2 Kontrola dźwięku"),
    anyTitle (1, "2.4.2 Tytuł strony"),
    htmlLang (1, "3.1.1 Język strony"),
    contact  (3, "5.1.2 Kontakt"),
    headerContent (3, "2.4.10 Nagłówki sekcji"),
    formattingTags (1, "1.3.1 Informacje i jej związki")
    ;

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

    public static boolean exists(String metricName){
        try {
            valueOf(metricName);
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    public String getNamePl() {
        return namePl;
    }

    public int getWeight() {
        return weight;
    }

    public int getOrder(){
        return ordinal();
    }

}
