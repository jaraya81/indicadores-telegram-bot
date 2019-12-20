package com.github.jaraya81.telegram.enums;

public enum Msg {
    USER_PLAY("USER_PLAY"),
    USER_PAUSE("USER_PAUSE"),
    USD_RESULT("USD_RESULT"),
    ;

    private String code;

    Msg(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
