package com.github.jaraya81.telegram.bot;

import com.github.jaraya81.telegram.component.MsgProcess;
import com.github.jaraya81.telegram.exception.TelegramException;
import com.github.jaraya81.telegram.operation.*;
import com.github.jaraya81.telegram.repo.UserRepo;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class IndicadoresBot {

    private UserRepo userRepo = new UserRepo();
    private String token;
    private TelegramBot bot;
    private MsgProcess msg = new MsgProcess();

    private START start;
    private PLAY play;
    private USD usd;
    private EUR eur;
    private BTC btc;

    private PAUSE pause;

    public IndicadoresBot(String token) throws TelegramException {
        super();
        this.token = token;
        log.info("init bot execution...");
        bot = new TelegramBot(token);
        start = START.builder().bot(bot).msg(msg).userRepo(userRepo).build();
        play = PLAY.builder().bot(bot).msg(msg).userRepo(userRepo).build();
        usd = USD.builder().bot(bot).msg(msg).userRepo(userRepo).build();
        eur = EUR.builder().bot(bot).msg(msg).userRepo(userRepo).build();
        btc = BTC.builder().bot(bot).msg(msg).userRepo(userRepo).build();
        pause = PAUSE.builder().bot(bot).msg(msg).userRepo(userRepo).build();
    }

    public IndicadoresBot end() throws TelegramException {
        log.info("Ejecución finalizada");
        userRepo.close();
        return this;
    }

    public IndicadoresBot run() throws TelegramException {
        GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().limit(100).offset(0).timeout(0));
        for (Update update : updatesResponse.updates()) {
            log.info("Exec update: " + update.updateId());
            log.debug(update.toString());
            start.exec(update);
            play.exec(update);
            usd.exec(update);
            eur.exec(update);
            btc.exec(update);
            pause.exec(update);
            bot.execute(new GetUpdates().limit(0).offset(update.updateId() + 1).timeout(0));
        }
        return this;
    }


    private List<Update> getUpdatesBy(List<Update> updates, String s) {
        if (Objects.isNull(updates)) {
            return null;
        }
        return updates.stream().filter(x -> x.message().text().startsWith(s)).collect(Collectors.toList());
    }

}
