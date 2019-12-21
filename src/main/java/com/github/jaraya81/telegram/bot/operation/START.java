package com.github.jaraya81.telegram.bot.operation;

import com.github.jaraya81.telegram.bot.MsgProcess;
import com.github.jaraya81.telegram.enums.Command;
import com.github.jaraya81.telegram.enums.Msg;
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
public class START {

    private UserRepo userRepo;
    private TelegramBot bot;
    private MsgProcess msg;

    public START exec(Update update) throws Exception {
        if (Objects.nonNull(update) && Objects.nonNull(update.message().text()) &&
                (update.message().text().contentEquals(Command.START.code()))) {
            String lang = update.message().from().languageCode();
            SendMessage request = new SendMessage(update.message().chat().id(), msg.msg(Msg.START, lang))
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true)
                    .replyMarkup(keyboard());
            SendResponse sendResponse = bot.execute(request);
            log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
        }
        return this;

    }


    private static Keyboard keyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(Command.PLAY.code()),
                }
        ).resizeKeyboard(true).selective(true);
    }
}