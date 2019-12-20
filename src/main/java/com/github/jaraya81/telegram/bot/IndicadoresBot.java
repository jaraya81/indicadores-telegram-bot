package com.github.jaraya81.telegram.bot;

import com.github.jaraya81.telegram.enums.Command;
import com.github.jaraya81.telegram.enums.Msg;
import com.github.jaraya81.telegram.enums.State;
import com.github.jaraya81.telegram.model.User;
import com.github.jaraya81.telegram.repo.UserRepo;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
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
            log.debug(update.toString());
            play(update);
            usd(update);
            pause(update);

            bot.execute(new GetUpdates().limit(0).offset(update.updateId() + 1).timeout(0));
        }


        return this;
    }

    private void usd(Update update) throws Exception {
        if (Objects.nonNull(update.message().text()) && update.message().text().startsWith(Command.GET_USD.code())) {
            User user = userRepo.getByIdUser(update.message().from().id().longValue());
            if (Objects.nonNull(user) && user.getState().contentEquals(State.SUBSCRIBE.name())) {
                SendMessage request = new SendMessage(update.message().chat().id(), Msg.USD_RESULT.code() + ": " + 589)
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(false);
                SendResponse sendResponse = bot.execute(request);
                log.info(sendResponse.toString());
                log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());

            }
        }
    }

    private void pause(Update update) throws Exception {
        if (Objects.nonNull(update.message().text()) &&
                (update.message().text().startsWith(Command.PAUSE_SLASH.code()) || update.message().text().startsWith(Command.PAUSE.code()))) {
            User user = userRepo.getByIdUser(update.message().from().id().longValue());
            if (Objects.isNull(user) || user.getState().contentEquals(State.UNSUBSCRIBE.name())) {
                return;
            }
            log.info("Desuscribiendo usuario: " + update.message().from().id() + " :: " + update.message().from().username());
            user.setState(State.UNSUBSCRIBE.name());
            userRepo.save(user);

            SendMessage request = new SendMessage(update.message().chat().id(), Msg.USER_PAUSE.code())
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .replyMarkup(KeyboardScene.unsubscribe())
                    .disableNotification(true);
            SendResponse sendResponse = bot.execute(request);
            log.info(sendResponse.toString());
            log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
        }
    }

    private void play(Update update) throws Exception {
        if (Objects.nonNull(update.message().text()) &&
                (update.message().text().startsWith(Command.PLAY_SLASH.code()) || update.message().text().startsWith(Command.PLAY.code()))) {
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

                SendMessage request = new SendMessage(update.message().chat().id(), Msg.USER_PLAY.code())
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true)
                        .replyMarkup(KeyboardScene.subscribe());
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
