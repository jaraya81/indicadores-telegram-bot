package cl.robotina.telegram.enums;

public enum Msg {
    USER_SUBSCRIBE("USER_SUBSCRIBE"),
    USER_UNSUBSCRIBE("USER_UNSUBSCRIBE"),

    ;

    private String code;

    Msg(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
