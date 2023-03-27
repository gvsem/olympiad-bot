package org.gvsem.olympiadbot.controller;

public enum QueryEnum {
    START("start"),
    GET_ALL_OLYMPIADS("get_all_olympiads"),
    GET_OLYMPIAD("get_olympiad"),
    GET_OLYMPIADS_BY_PROFILE("get_olympiads_by_profile");

    private final String message;

    QueryEnum(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
