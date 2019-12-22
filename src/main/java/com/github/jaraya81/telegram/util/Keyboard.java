package com.github.jaraya81.telegram.util;

import com.github.jaraya81.telegram.enums.Command;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;

public class Keyboard {

    public static com.pengrad.telegrambot.model.request.Keyboard play() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(Command.GET_USD.code()),
                        new KeyboardButton(Command.GET_EUR.code()),
                        new KeyboardButton(Command.GET_BTC.code()),
                        new KeyboardButton(Command.PAUSE.code()),
                }
        ).resizeKeyboard(true).selective(true);
    }

    public static com.pengrad.telegrambot.model.request.Keyboard banned() {
        return new ReplyKeyboardRemove();
    }

    public static com.pengrad.telegrambot.model.request.Keyboard start() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(Command.PLAY.code()),
                }
        ).resizeKeyboard(true).selective(true);
    }

    public static com.pengrad.telegrambot.model.request.Keyboard pause() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton(Command.PLAY.code())
                }
        ).resizeKeyboard(true).selective(true);
    }
}
