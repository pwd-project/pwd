package org.pwd.domain.audit;

import java.util.Optional;

/**
 * @author bartosz.walacik
 */
public enum Metric {
    alt(1, "Tekst alternatywny", "1.1.1", "http://fdc.org.pl/wcag2/#text-equiv"),
    formattingTags(1, "Gęstość tagów formatujących", "1.3.1", "http://fdc.org.pl/wcag2/#content-structure-separation"),
    sound(1, "Dźwięk na przy wyświetlaniu strony", "1.4.2", "http://fdc.org.pl/wcag2/#visual-audio-contrast"),
    bigFont(3, "Zmiana rozmiaru tekstu", "1.4.4", "http://fdc.org.pl/wcag2/#visual-audio-contrast"),
    anyTitle(1, "Tytuł strony", "2.4.2", "http://fdc.org.pl/wcag2/#navigation-mechanisms"),
    headerContent(3, "Nagłówki sekcji", "2.4.10", "http://fdc.org.pl/wcag2/#navigation-mechanisms"),
    html(3, "Poprawność HTML", "W3C", "https://validator.w3.org/"),
    htmlLang(1, "Język strony", "3.1.1", "http://fdc.org.pl/wcag2/#meaning"),
    contact(3, "Kontakt", "", ""),
    cms(0, "Użycie CMS", "", ""),

    // Metryki Googlowe
    badAriaRole(1, "Elementy ARIA z rolami muszą być poprawne", "AX_ARIA_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_01"),
    controlsWithoutLabel(1, "Bez labelek", "AX_TEXT_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_text-01"),
    nonExistentAriaRelatedElement(1, "Atrybut ARIA powiązany", "AX_ARIA_02", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_02"),
    requiredAriaAttributeMissing(1, "Wymagane atrybuty dlaelementów z rolami ARIA", "AX_ARIA_03", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_03"),
    badAriaAttributeValue(1, "Stan i poprawność właściwości elementów ARIA", "AX_ARIA_04", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_04"),
    mainRoleOnInappropriateElement(2, "Atrybut role=main tylko na kluczowych elementach", "AX_ARIA_05", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_05"),
    ariaOwnsDescendant(2, "Zbędny atrybut aria-owns", "AX_ARIA_06", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_06"),
    multipleAriaOwners(2, "Wielokrotne używanie atrybutu aria-owns", "AX_ARIA_07", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_07"),
    requiredOwnedAriaRoleMissing(1, "Właściwe role elementów podrzędnych", "AX_ARIA_08", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_08"),
    ariaRoleNotScoped(1, "Właściwy zakres elementów ARIA", "AX_ARIA_09", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_09"),
    unsupportedAriaAttribute(1, "Obsługiwane atrybuty ARIA", "AX_ARIA_10", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_10"),
    badAriaAttribute(2, "Prawidłowe atrybuty ARIA", "AX_ARIA_11", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_11"),
    ariaOnReservedElement(2, "Wsparcie dla ról, stanów i właściwości ARIA", "AX_ARIA_12", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_aria_12"),
    audioWithoutControls(2, "Elementy audio posiadają kontrolki", "AX_AUDIO_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_audio_01"),
    duplicateId(1, "Unikalność identyfikatorów elementów", "AX_HTML_02", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_html_02"),
    multipleLabelableElementsPerLabel(1, "Etykietowanie elementów", "AX_TEXT_03", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_text_03"),
    linkWithUnclearPurpose(2, "Cel odnośnika czytelny na bazie opisu", "AX_TEXT_04", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_text_04"),
    elementsWithMeaningfulBackgroundImage(2, "Znaczące obrazy nie są tłem", "AX_IMAGE_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_image_01"),
    lowContrastElements(1, "Niski kontrast", "AX_COLOR_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_color_01"),
    focusableElementNotVisibleAndNotAriaHidden(0, "Niewidoczne elementy z focusem", "AX_FOCUS_01", "https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#ax_focus_01");

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
            case 1:
                return "A";
            case 2:
                return "AA";
            case 3:
                return "AAA";
            default:
                return "";
        }
    }

}
