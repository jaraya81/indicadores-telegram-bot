package com.github.jaraya81.telegram.operation;

import com.github.jaraya81.telegram.component.MsgProcess;
import com.github.jaraya81.telegram.enums.Command;
import com.github.jaraya81.telegram.enums.Msg;
import com.github.jaraya81.telegram.enums.State;
import com.github.jaraya81.telegram.exception.TelegramException;
import com.github.jaraya81.telegram.model.User;
import com.github.jaraya81.telegram.repo.UserRepo;
import com.github.jaraya81.telegram.util.Keyboard;
import com.github.jaraya81.telegram.util.StateUtil;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Builder
@Slf4j
public class START {

    private UserRepo userRepo;
    private TelegramBot bot;
    private MsgProcess msg;

    public START exec(Update update) throws TelegramException {
        if (Objects.nonNull(update) && Objects.nonNull(update.message().text()) &&
                (update.message().text().contentEquals(Command.START.code()))) {
            start(update);

        }
        return this;
    }

    private void start(Update update) throws TelegramException {
        User user = userRepo.getByIdUser(update.message().from().id().longValue());
        String lang = update.message().from().languageCode();
        log.info(State.PLAY.name() + " :: " + update.message().from().id() + " :: " + update.message().from().username());
        if (Objects.isNull(user)) {
            user = User.builder()
                    .idUser(update.message().from().id().longValue())
                    .username(update.message().from().username())
                    .state(State.NEW_USER.name())
                    .build();
            userRepo.save(user);
            SendResponse sendResponse = bot.execute(new SendMessage(update.message().chat().id(), msg.msg(Msg.START_OK, lang))
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true)
                    .replyMarkup(Keyboard.start()));
            log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
        } else if (StateUtil.isBanned(user)) {
            SendResponse sendResponse = bot.execute(new SendMessage(update.message().chat().id(), msg.msg(Msg.START_BANNED_USER, lang))
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true)
                    .replyMarkup(Keyboard.banned()));
            log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
        } else {
            user.setState(State.NEW_USER.name());
            userRepo.save(user);
            SendResponse sendResponse = bot.execute(new SendMessage(update.message().chat().id(), msg.msg(Msg.START_AGAIN, lang))
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true)
                    .replyMarkup(Keyboard.start()));
            log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
        }

    }


}