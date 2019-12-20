package cl.robotina.telegram.bot;

import cl.robotina.telegram.enums.Msg;
import cl.robotina.telegram.enums.State;
import cl.robotina.telegram.model.User;
import cl.robotina.telegram.repo.UserRepo;
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

    private UserRepo userRepo = new UserRepo();
    private String token;
    private TelegramBot bot;

    public IndicadoresBot(String token) throws Exception {
        super();
        this.token = token;
        log.info("init bot execution...");
        bot = new TelegramBot(token);

    }

    public IndicadoresBot end() throws Exception {
        log.info("Ejecucion finalizada");
        userRepo.close();
        return this;
    }

    public IndicadoresBot run() throws Exception {

        GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().limit(100).offset(0).timeout(0));
        for (Update update : updatesResponse.updates()) {
            log.info("Exec update: " + update.updateId());
            toSubscribe(update);
            toUnsubscribe(update);

            bot.execute(new GetUpdates().limit(0).offset(update.updateId() + 1).timeout(0));
        }


        return this;
    }

    private void toUnsubscribe(Update update) throws Exception {
        if (update.message().text().startsWith(COMMAND_UNSUBSCRIBE)) {
            User user = userRepo.getByIdUser(update.message().from().id().longValue());
            if (Objects.isNull(user) || user.getState().contentEquals(State.UNSUBSCRIBE.name())) {
                return;
            }
            log.info("Desuscribiendo usuario: " + update.message().from().id() + " :: " + update.message().from().username());
            user.setState(State.UNSUBSCRIBE.name());
            userRepo.save(user);
            SendMessage request = new SendMessage(update.message().chat().id(), Msg.USER_UNSUBSCRIBE.code())
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
            SendResponse sendResponse = bot.execute(request);
            log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
        }
    }


    private void toSubscribe(Update update) throws Exception {
        if (update.message().text().startsWith(COMMAND_SUBSCRIBE)) {
            User user = userRepo.getByIdUser(update.message().from().id().longValue());
            if (Objects.isNull(user) || !user.getState().contentEquals(State.SUBSCRIBE.name())) {
                log.info("Suscribiendo usuario: " + update.message().from().id() + " :: " + update.message().from().username());
                if (Objects.isNull(user)) {
                    user = User.builder()
                            .idUser(update.message().from().id().longValue())
                            .username(update.message().from().username())
                            .state(State.SUBSCRIBE.name())
                            .build();
                } else {
                    user.setState(State.SUBSCRIBE.name());
                }
                userRepo.save(user);
                SendMessage request = new SendMessage(update.message().chat().id(), Msg.USER_SUBSCRIBE.code())
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true);
                SendResponse sendResponse = bot.execute(request);
                log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
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
