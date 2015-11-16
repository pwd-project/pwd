package org.pwd.domain.audit;

import java.util.Optional;

/**
 * @author bartosz.walacik
 */
public enum Metric {
    alt(    3, "Tekst alternatywny", "1.1.1", "http://fdc.org.pl/wcag2/#text-equiv"),
    formattingTags(
            3, "Gęstość tagów formatujących", "1.3.1", "http://fdc.org.pl/wcag2/#content-structure-separation"),
    sound(  3, "Dźwięk na przy wyświetlaniu strony", "1.4.2", "http://fdc.org.pl/wcag2/#visual-audio-contrast"),
    contrast(
            2, "Kontrast", "1.4.3", "http://fdc.org.pl/wcag2/#visual-audio-contrast"),
    bigFont(2, "Zmiana rozmiaru tekstu", "1.4.4", "http://fdc.org.pl/wcag2/#visual-audio-contrast"),
    anyTitle(
            3, "Tytuł strony", "2.4.2", "http://fdc.org.pl/wcag2/#navigation-mechanisms"),
    headerContent(
            1, "Nagłówki sekcji", "2.4.10", "http://fdc.org.pl/wcag2/#navigation-mechanisms"),
    htmlLang(
            3, "Język strony", "3.1.1", "http://fdc.org.pl/wcag2/#meaning"),
    labels( 3, "Etykiety lub instrukcje", "3.3.2", "http://fdc.org.pl/wcag2/#minimize-error"),
    contact(1, "Kontakt", "", ""),
    // Metryki Googlowe
    cms(    0, "Użycie CMS", "", ""),
    aria(   3, "Elementy ARIA z rolami muszą być poprawne", "AX_ARIA_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_01"),
    nonExistentAriaRelatedElement(
            3, "Atrybut ARIA powiązany", "AX_ARIA_02", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_02"),
    requiredAriaAttributeMissing(
            3, "Wymagane atrybuty dla elementów z rolami ARIA", "AX_ARIA_03", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_03"),
    badAriaAttributeValue(
            3, "Stan i poprawność właściwości elementów ARIA", "AX_ARIA_04", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_04"),
    mainRoleOnInappropriateElement(
            2, "Atrybut role=main tylko na kluczowych elementach", "AX_ARIA_05", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_05"),
    ariaOwnsDescendant(
            2, "Zbędny atrybut aria-owns", "AX_ARIA_06", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_06"),
    multipleAriaOwners(
            2, "Wielokrotne używanie atrybutu aria-owns", "AX_ARIA_07", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_07"),
    requiredOwnedAriaRoleMissing(
            3, "Właściwe role elementów podrzędnych", "AX_ARIA_08", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_08"),
    ariaRoleNotScoped(
            3, "Właściwy zakres elementów ARIA", "AX_ARIA_09", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_09"),
    unsupportedAriaAttribute(
            3, "Obsługiwane atrybuty ARIA", "AX_ARIA_10", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_10"),
    badAriaAttribute(
            2, "Prawidłowe atrybuty ARIA", "AX_ARIA_11", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_11"),
    ariaOnReservedElement(
            2, "Wsparcie dla ról, stanów i właściwości ARIA", "AX_ARIA_12", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_12"),
    audioWithoutControls(
            2, "Elementy audio posiadają kontrolki", "AX_AUDIO_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_audio_01"),
    duplicateId(
            3, "Unikalność identyfikatorów elementów", "AX_HTML_02", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_html_02"),
    multipleLabelableElementsPerLabel(
            3, "Etykietowanie elementów", "AX_TEXT_03", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_text_03"),
    linkWithUnclearPurpose(
            2, "Cel odnośnika czytelny na bazie opisu", "AX_TEXT_04", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_text_04"),
    elementsWithMeaningfulBackgroundImage(
            2, "Znaczące obrazy nie są tłem", "AX_IMAGE_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_image_01"),
    focusableElementNotVisibleAndNotAriaHidden(
            0, "Niewidoczne elementy z focusem", "AX_FOCUS_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_focus_01");

    private String namePl;
    private int weight;
    private String guidelineName;
    private String guidelineHref;

    Metric(int weight, String namePl, String guidelineName, String guidelineHref) {
        this.namePl = namePl;
        this.weight = weight;
        this.guidelineName = guidelineName;
        this.guidelineHref = guidelineHref;
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

    public String getGuidelineName() {
        return guidelineName;
    }

    public String getGuidelineHref() {
        return guidelineHref;
    }

    public int getOrder() {
        return ordinal();
    }

    public String getWeightString() {
        switch (weight) {
            case 3: return "A";
            case 2: return "AA";
            case 1: return "AAA";
            default: return "";
        }
    }

}
