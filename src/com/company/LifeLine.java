package com.company;

/**
 * Ten Enum reprezentuje wszystkie możliwe koła ratunkowe i ich reprezentacje tekstowe.
 */
public enum LifeLine {
    FIFTY_FIFTY("50/50"),
    POLL_AUDIENCE("Pytanie do publiczności"),
    CALL_A_FRIEND("Telefon do przyjaciela");

    private String text;

    private LifeLine(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}