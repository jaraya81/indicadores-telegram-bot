package com.github.jaraya81.telegram.enums;

public enum Msg {
    START_OK("WELCOME_START"),
    START_BANNED_USER("START_BANNED_USER"),
    START_AGAIN("START_AGAIN"),
    USER_PLAY("USER_PLAY"),
    USER_PAUSE("USER_PAUSE"),
    USD_RESULT("USD_RESULT"),
    BTC_RESULT("BTC_RESULT"),
    EUR_RESULT("EUR_RESULT"),
    ;

    private String code;

    Msg(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
