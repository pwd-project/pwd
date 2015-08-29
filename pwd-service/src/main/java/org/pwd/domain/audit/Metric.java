package org.pwd.domain.audit;

import java.util.Optional;

/**
 * @author bartosz.walacik
 */
public enum Metric {
    anyTitle (1, "tytuł"),
    alt      (2, "text alternatywny"),
    htmlLang (1, "język strony")
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
