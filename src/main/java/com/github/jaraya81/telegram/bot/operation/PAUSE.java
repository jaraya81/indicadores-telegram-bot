package com.github.jaraya81.telegram.bot.operation;

import com.github.jaraya81.telegram.bot.MsgProcess;
import com.github.jaraya81.telegram.enums.Command;
import com.github.jaraya81.telegram.enums.Msg;
import com.github.jaraya81.telegram.enums.State;
import com.github.jaraya81.telegram.model.User;
import com.github.jaraya81.telegram.repo.UserRepo;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Builder
@Slf4j
public class PAUSE {

    private UserRepo userRepo;
    private TelegramBot bot;
    private MsgProcess msg;

    public PAUSE exec(Update update) throws Exception {
        if (Objects.nonNull(update) && Objects.nonNull(update.message().text()) &&
                update.message().text().contentEquals(Command.PAUSE.code())) {
            User user = userRepo.getByIdUser(update.message().from().id().longValue());
            String lang = update.message().from().languageCode();
            if (Objects.nonNull(user) && !user.getState().contentEquals(State.UNSUBSCRIBE.name())) {
                log.info("Desuscribiendo usuario: " + update.message().from().id() + " :: " + update.message().from().username());
                user.setState(State.UNSUBSCRIBE.name());
                userRepo.save(user);

                SendMessage request = new SendMessage(update.message().chat().id(), msg.msg(Msg.USER_PAUSE, lang))
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .replyMarkup(unsubscribe())
                        .disableNotification(true);
                SendResponse sendResponse = bot.execute(request);
                log.info(sendResponse.toString());
                log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
            }
        }
        return this;
    }

    private static Keyboard unsubscribe() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(Command.PLAY.code())
                }
        ).resizeKeyboard(true).selective(true);
    }
}
