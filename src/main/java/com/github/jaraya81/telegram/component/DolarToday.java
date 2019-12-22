package com.github.jaraya81.telegram.component;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class DolarToday {

    //TODO HACER
    public static Double getEUR() {
        return 983943.0;
    }

    public static Double getUSD() {
        return 1223943.0;

    }

    public DolarToday exec() {
        log.info("Consultando indicadores en DT...");
        //TODO CONSULTAR JSON
        return this;
    }


    public static Double getBTC() {
        return 8993849349.0;
    }
}
