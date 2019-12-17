package cl.robotina.telegram;

import cl.robotina.telegram.bot.IndicadoresBot;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

/**
 * Simple bot
 */
public class App {


    @SneakyThrows
    public static void main(String[] args) {

        Options options = new Options();
        options.addOption(Option.builder()
                .required()
                .hasArg()
                .argName("token")
                .longOpt("token")
                .desc("Token del Telegram Bot.")
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        String token = commandLine.getOptionValue("token");
        if (token == null || token.isEmpty()) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Indicadores Telegram Bot", options);
            throw new RuntimeException("token is empty");
        }
        new IndicadoresBot(token).run();
    }
}
