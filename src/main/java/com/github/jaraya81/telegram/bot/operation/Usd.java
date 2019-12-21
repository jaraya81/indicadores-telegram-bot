package com.github.jaraya81.telegram.bot.operation;

import com.github.jaraya81.telegram.bot.MsgProcess;
import com.github.jaraya81.telegram.enums.Command;
import com.github.jaraya81.telegram.enums.Msg;
import com.github.jaraya81.telegram.enums.State;
import com.github.jaraya81.telegram.model.User;
import com.github.jaraya81.telegram.repo.UserRepo;
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
public class USD {

    private UserRepo userRepo;
    private TelegramBot bot;
    private MsgProcess msg;

    public USD exec(Update update) throws Exception {
        if (Objects.nonNull(update) && Objects.nonNull(update.message().text()) && update.message().text().contentEquals(Command.GET_USD.code())) {
            User user = userRepo.getByIdUser(update.message().from().id().longValue());
            String lang = update.message().from().languageCode();
            if (Objects.nonNull(user) && user.getState().contentEquals(State.SUBSCRIBE.name())) {
                SendMessage request = new SendMessage(update.message().chat().id(), msg.msg(Msg.USD_RESULT, lang)  + getUSD())
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .disableNotification(false);
                SendResponse sendResponse = bot.execute(request);
                log.info(sendResponse.toString());
                log.info((sendResponse.isOk() ? "OK" : "NOK") + " :: " + update.message().chat().id());

            }
        }
        return this;
    }

    private Double getUSD() {
        //TODO HACER
        return 589.0;
    }

}
