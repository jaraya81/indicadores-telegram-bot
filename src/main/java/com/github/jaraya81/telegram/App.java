package com.github.jaraya81.telegram;

import com.github.jaraya81.telegram.bot.IndicadoresBot;
import com.github.jaraya81.telegram.component.DolarToday;
import com.github.jaraya81.telegram.exception.TelegramException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Bot base, registra usuarios en una base de datos.
 */
@Slf4j
public class App {

    private static final String TOKEN = "token";
    private ExecutorService executor
            = Executors.newSingleThreadExecutor();

    @SneakyThrows
    public static void main(String[] args) {

        Options options = new Options();
        options.addOption(Option.builder()
                .required()
                .hasArg()
                .longOpt(TOKEN)
                .argName(TOKEN)
                .desc("Token del Telegram Bot.")
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        String token = commandLine.getOptionValue(TOKEN);
        if (token == null || token.isEmpty()) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Indicadores Telegram Bot", options);
            throw new RuntimeException("token is empty");
        }

        new App().getDT();

        while (true) {
            try {
                new IndicadoresBot(token).run().end();
                log.info("Sleeping...");
            } catch (TelegramException e) {
                log.error("", e);
            }
            Thread.sleep(30000);

        }

    }

    public Future<Void> getDT() {
        return executor.submit(() -> {
            while (true) {
                try {
                    DolarToday dt = DolarToday.builder().build().exec();
                    Thread.sleep(5 * 60 * 1000);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        });
    }
}
