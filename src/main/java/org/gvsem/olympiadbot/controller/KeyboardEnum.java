package org.gvsem.olympiadbot.controller;

public enum KeyboardEnum {

    DEMO_FIND_ALL("Find all olympiads"),
    DEMO_FIND_SUBJECT("Find olympiads in 'математика'"),
    DEMO_FIND_OLYMPIAD("Show tracks of 'Физтех'"),
    MENU_FIND_ALL("All olympiads"),
    MENU_FIND_SUBJECT("Find by subject"),
    NEXT_PAGE("Next page >>");

    private final String message;

    KeyboardEnum(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
