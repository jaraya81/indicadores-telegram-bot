package com.github.jaraya81.telegram.bot;

import com.github.jaraya81.telegram.enums.Command;
import com.pengrad.telegrambot.model.request.*;

public class KeyboardScene {
    public static Keyboard subscribe() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(Command.GET_USD.code()),
                        new KeyboardButton(Command.GET_EUR.code()),
                        new KeyboardButton(Command.GET_BTC.code()),
                        new KeyboardButton(Command.PAUSE.code()),
                }
        ).resizeKeyboard(true).selective(true);
    }

    public static Keyboard unsubscribe() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(Command.PLAY.code())
                }
        ).resizeKeyboard(true).selective(true);
    }
}
