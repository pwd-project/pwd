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
    contact(3, "5.1.2 Kontakt"),
    nonExistentAriaRelatedElement(1, "AX_ARIA_02 Atrybut aria-labelledby"),
    requiredAriaAttributeMissing(1, "AX_ARIA_03 Wymagane atrybuty dla elementów z rolami ARIA"),
    badAriaAttributeValue(1, "AX_ARIA_04 Stan i poprawność właściwości elementów ARIA"),
    mainRoleOnInappropriateElement(2, "AX_ARIA_05 Atrybut role=main powinien występować tylko na kluczowych elementach"),
    ariaOwnsDescendant(2, "AX_ARIA_06 Atrybut aria-owns nie powinien być używany, jeżeli własność wynika bezpośrednio z DOM"),
    multipleAriaOwners(2, "AX_ARIA_07 Identyfikator elementu nie może występować więcej niż jeden raz jako wartość atrybutu aria-owns"),
    requiredOwnedAriaRoleMissing(1, "AX_ARIA_08 Właściwe role elementów podrzędnych"),
    ariaRoleNotScoped(1, "AX_ARIA_09 Zakres elementów ARIA"),
    unsupportedAriaAttribute(1, "AX_ARIA_10 Obsługiwane atrybuty ARIA"),
    badAriaAttribute(2, "AX_ARIA_11 Istnieją elementy z nieprawidłowymi atrybutami ARIA"),
    ariaOnReservedElement(2, "AX_ARIA_12 Element nie wspiera ról, stanów i właściwości ARIA"),
    audioWithoutControls(2, "AX_AUDIO_01 Elementy związane z audio powinny mieć kontrolki"),
    duplicateId(1, "AX_HTML_02 Unikalność identyfikatorów elementów"),
    multipleLabelableElementsPerLabel(1, "AX_TEXT_03 Etykietowanie elementów"),
    linkWithUnclearPurpose(2, "AX_TEXT_04 Cel każdego odnośnika powinien być czytelny z jego opisu"),
    elementsWithMeaningfulBackgroundImage(2, "AX_IMAGE_01 Obrazki mające znaczenie nie powinny być stosowane jako tło"),
    focusableElementNotVisibleAndNotAriaHidden(2, "AX_FOCUS_01 Istnieją elementy, które mogą uzyskać fokus, ale są niewidoczne albo zakryte przez inne elementy");

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
