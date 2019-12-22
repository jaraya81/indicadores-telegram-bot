package com.github.jaraya81.telegram.operation;

import com.github.jaraya81.telegram.component.DolarToday;
import com.github.jaraya81.telegram.exception.TelegramException;
import com.github.jaraya81.telegram.util.StateUtil;
import com.github.jaraya81.telegram.component.MsgProcess;
import com.github.jaraya81.telegram.enums.Command;
import com.github.jaraya81.telegram.enums.Msg;
import com.github.jaraya81.telegram.enums.State;
import com.github.jaraya81.telegram.model.User;
import com.github.jaraya81.telegram.repo.UserRepo;
import com.github.jaraya81.telegram.util.CommandUtil;
import com.github.jaraya81.telegram.util.Keyboard;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class BTC {

    private UserRepo userRepo;
    private TelegramBot bot;
    private MsgProcess msg;

    public BTC exec(Update update) throws TelegramException {
        if (CommandUtil.check(update, Command.GET_BTC)) {
            User user = userRepo.getByIdUser(update.message().from().id().longValue());
            String lang = update.message().from().languageCode();
            if (User.exist(user) && !StateUtil.isBanned(user) && StateUtil.is(user, State.PLAY)) {
                log.info(Command.GET_BTC.name() + " :: " + update.message().from().id() + " :: " + update.message().from().username());
                SendResponse sendResponse = bot.execute(new SendMessage(update.message().chat().id(), msg.msg(Msg.BTC_RESULT, lang) + getBTC())
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true)
                        .replyMarkup(Keyboard.play()));
                log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
            }
        }
        return this;
    }

    private Double getBTC() {
        return DolarToday.getBTC();
    }

}
