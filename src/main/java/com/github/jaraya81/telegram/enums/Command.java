package com.github.jaraya81.telegram.enums;

public enum Command {

    START("/start"),
    PLAY("PLAY"),
    PAUSE("PAUSE"),
    GET_USD("USD"),
    GET_EUR("EUR"),
    GET_BTC("BTC"),
    ;

    private String code;

    Command(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

}
