package org.pwd.api;

/**
 * @author bartosz.walacik
 */
class Welcome {
    private final String message;

    public Welcome(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
