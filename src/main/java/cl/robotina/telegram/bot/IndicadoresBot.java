package cl.robotina.telegram.bot;

import cl.robotina.telegram.App;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class IndicadoresBot {

    private String token;

    public IndicadoresBot(String token) {
        super();
        this.token = token;
    }

    public IndicadoresBot run() {
        TelegramBot bot = new TelegramBot(token);

        GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().limit(100).offset(0).timeout(0));
        List<Update> updates = updatesResponse.updates();
        Integer lastUpdateId = null;
        for (Update update : updates) {
            lastUpdateId = update.updateId();
            Message message = update.message();
            Long chatId = update.message().chat().id();
            String text = update.message().text();

            log.info(message.text());
            log.info(message.toString());
            SendMessage request = new SendMessage(chatId, "R: " + text)
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
            SendResponse sendResponse = bot.execute(request);
            log.info(sendResponse.toString());

            if (sendResponse.isOk()) {
                log.info(chatId + ":: OK");
            } else {
                log.error(chatId + ":: NOK");
            }
        }

        if (lastUpdateId != null) {
            bot.execute(new GetUpdates().limit(0).offset(lastUpdateId + 1).timeout(0));
        }
        return this;
    }

}
