package com.github.jaraya81.telegram.operation;

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
public class PAUSE {

    private UserRepo userRepo;
    private TelegramBot bot;
    private MsgProcess msg;

    public PAUSE exec(Update update) throws TelegramException {
        if (CommandUtil.check(update, Command.PAUSE)) {
            User user = userRepo.getByIdUser(update.message().from().id().longValue());
            String lang = update.message().from().languageCode();
            if (User.exist(user) && !StateUtil.isBanned(user) && !StateUtil.is(user, State.PAUSE)) {
                log.info(State.PAUSE.name() + " :: " + update.message().from().id() + " :: " + update.message().from().username());
                user.setState(State.PAUSE.name());
                userRepo.save(user);
                SendResponse sendResponse = bot.execute(new SendMessage(update.message().chat().id(), msg.msg(Msg.USER_PAUSE, lang))
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(true)
                        .replyMarkup(Keyboard.pause()));
                log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());
            }
        }
        return this;
    }


}
