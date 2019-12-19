package cl.robotina.telegram;

import cl.robotina.telegram.bot.IndicadoresBot;
import lombok.SneakyThrows;
import org.apache.commons.cli.*;

/**
 * Bot base, registra usuarios en una base de datos.
 */
public class App {


    private static final String TOKEN = "token";

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
        new IndicadoresBot(token).run();
    }
}
