package org.gvsem.olympiadbot.controller;

public enum MessageEnum {
    START("Hello, olimpiadnik!\n\nFind school olympiads using Olympiad Bot.\nWhat bot can do:"),
    ERROR("Error, your query is malformed"),
    ID_REQUIRED("Please, provide id of olympiad to find: /" + QueryEnum.GET_OLYMPIAD + " 5"),
    PROFILE_REQUIRED("Please, provide profile of olympiads to find: /" + QueryEnum.GET_OLYMPIADS_BY_PROFILE + " математика"),
    OLYMPIAD_NOT_FOUND("Olympiad with id %d is not found"),
    OLYMPIADS_NOT_FOUND("No olympiads on page %d"),
    INTERNAL_ERROR("Something really strange went wrong.");

    private final String message;

    MessageEnum(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
