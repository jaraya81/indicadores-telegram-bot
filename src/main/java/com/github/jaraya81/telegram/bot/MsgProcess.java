package com.github.jaraya81.telegram.bot;

import com.github.jaraya81.telegram.enums.Msg;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MsgProcess {

    private static final String EN = "en";
    private static final String ES = "es";

    private Map<String, Map<Msg, String>> languages = new HashMap<>();

    public MsgProcess() {
        super();
        languages();
    }

    private void languages() {
        languages.put("en", en());
        languages.put("es", es());
    }

    private Map<Msg, String> es() {
        Map<Msg, String> es = new HashMap<>();
        es.put(Msg.START, "Bienvenidos! Presiona el botón PLAY para registrarte!... \n"
                + "Actualmente el bot es un poco lento para responderte, "
                + "se un poco paciente (aprox 30 segundos de espera).");
        es.put(Msg.USER_PLAY, "¡Registrado! Dentro de poco empezarás a recibir notificaciones.");
        es.put(Msg.USER_PAUSE, "¡Has pausado las notificaciones del robot!");
        es.put(Msg.USD_RESULT, "El dólar (USD) se cotiza en: ");
        return es;
    }

    private Map<Msg, String> en() {
        Map<Msg, String> en = new HashMap<>();

        return en;

    }

    public String msg(Msg msg, String lang) {
        if (Objects.isNull(msg)) {
            return null;
        }

        String translate = languages.get(langAvailable(lang) ? lang : EN).get(msg);
        return translate != null ? translate : msg.code();
    }

    private boolean langAvailable(String lang) {
        if (Objects.isNull(lang) || lang.isEmpty()) {
            return false;
        }
        return languages.get(lang) != null;
    }
}
