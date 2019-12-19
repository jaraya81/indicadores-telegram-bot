package cl.robotina.telegram.bot;

import cl.robotina.telegram.enums.State;
import cl.robotina.telegram.model.User;
import cl.robotina.telegram.repo.Repo;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class IndicadoresBot {

    private static final String COMMAND_SUBSCRIBE = "/habilitar";
    private static final String COMMAND_UNSUBSCRIBE = "/deshabilitar";

    private Repo repo = new Repo();
    private String token;
    private TelegramBot bot;

    public IndicadoresBot(String token) throws Exception {
        super();
        this.token = token;
        log.info("init bot execution...");
        bot = new TelegramBot(token);

    }

    public IndicadoresBot run() throws Exception {

        GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().limit(100).offset(0).timeout(0));
        Integer lastUpdateId = null;
        for (Update update : updatesResponse.updates()) {
            lastUpdateId = update.updateId();
            toSubscribe(update);
            toUnsubscribe(update);
        }

        if (lastUpdateId != null) {
            bot.execute(new GetUpdates().limit(0).offset(lastUpdateId + 1).timeout(0));
        }

        return this;
    }

    private void toUnsubscribe(Update update) throws Exception {
        if (update.message().text().startsWith(COMMAND_UNSUBSCRIBE)) {
            User user = repo.getByIdUser(update.message().from().id().longValue());
            if (Objects.isNull(user) || user.getState().contentEquals(State.UNSUBSCRIBE.name())) {
                return;
            }
            log.info("Desuscribiendo usuario: " + update.message().from().username());
            log.info("User ID:" + update.message().from().id());
            log.info(update.toString());
            user.setState(State.UNSUBSCRIBE.name());
            repo.save(user);
            SendMessage request = new SendMessage(update.message().chat().id(), "Usuario desuscrito en plataforma")
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
            SendResponse sendResponse = bot.execute(request);
            log.info(update.message().chat().id() + (sendResponse.isOk() ? "OK" : "NOK"));
        }
    }


    private void toSubscribe(Update update) throws Exception {
        if (update.message().text().startsWith(COMMAND_SUBSCRIBE)) {
            User user = repo.getByIdUser(update.message().from().id().longValue());
            if (Objects.isNull(user) || !user.getState().contentEquals(State.SUBSCRIBE.name())) {
                log.info("Suscribiendo usuario: " + update.message().from().username());
                log.info("User ID:" + update.message().from().id());
                log.info(update.toString());
                if (Objects.isNull(user)) {
                    user = User.builder()
                            .idUser(update.message().from().id().longValue())
                            .username(update.message().from().username())
                            .state(State.SUBSCRIBE.name())
                            .build();
                } else {
                    user.setState(State.SUBSCRIBE.name());
                }
                repo.save(user);
                SendMessage request = new SendMessage(update.message().chat().id(), "Usuario suscrito en plataforma")
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true);
                SendResponse sendResponse = bot.execute(request);
                log.info(update.message().chat().id() + (sendResponse.isOk() ? "OK" : "NOK"));
            }

        }
    }

    private List<Update> getUpdatesBy(List<Update> updates, String s) {
        if (Objects.isNull(updates)) {
            return null;
        }
        return updates.stream().filter(x -> x.message().text().startsWith(s)).collect(Collectors.toList());
    }

}
