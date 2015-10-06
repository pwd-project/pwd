package org.pwd.domain.audit;

import java.util.Optional;

/**
 * @author bartosz.walacik
 */
public enum Metric {
    alt(1, "Tekst alternatywny"),
    formattingTags(1, "Gęstość tagów formatujących"),
    sound(1, "Dźwięk na przy wyświetlaniu strony"),
    contrast(2, "Kontrast"),
    bigFont(2, "Zmiana rozmiaru tekstu"),
    anyTitle(1, "Tytuł strony"),
    headerContent(3, "Nagłówki sekcji"),
    htmlLang(1, "Język strony"),
    labels(1, "Etykiety lub instrukcje"),
    contact(3, "Kontakt"),
    // Metryki Googlowe
    nonExistentAriaRelatedElement(
            1, "Atrybut ARIA powiązany"),
    requiredAriaAttributeMissing(
            1, "Wymagane atrybuty dla elementów z rolami ARIA"),
    badAriaAttributeValue(
            1, "Stan i poprawność właściwości elementów ARIA"),
    mainRoleOnInappropriateElement(
            2, "Atrybut role=main tylko na kluczowych elementach"),
    ariaOwnsDescendant(
            2, "Zbędny atrybut aria-owns"),
    multipleAriaOwners(
            2, "Wielokrotne używanie atrybutu aria-owns"),
    requiredOwnedAriaRoleMissing(
            1, "Właściwe role elementów podrzędnych"),
    ariaRoleNotScoped(
            1, "Właściwy zakres elementów ARIA"),
    unsupportedAriaAttribute(
            1, "Obsługiwane atrybuty ARIA"),
    badAriaAttribute(
            2, "Prawidłowe atrybuty ARIA"),
    ariaOnReservedElement(
            2, "Wsparcie dla ról, stanów i właściwości ARIA"),
    audioWithoutControls(
            2, "Elementy audio posiadają kontrolki"),
    duplicateId(
            1, "Unikalność identyfikatorów elementów"),
    multipleLabelableElementsPerLabel(
            1, "Etykietowanie elementów"),
    linkWithUnclearPurpose(
            2, "Cel odnośnika czytelny na bazie opisu"),
    elementsWithMeaningfulBackgroundImage(
            2, "Znaczące obrazy nie są tłem"),
    focusableElementNotVisibleAndNotAriaHidden(
            2, "Niewidoczne elementy z focusem");

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
