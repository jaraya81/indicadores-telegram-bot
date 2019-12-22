package com.github.jaraya81.telegram.util;

import com.github.jaraya81.telegram.enums.Command;
import com.pengrad.telegrambot.model.Update;

import java.util.Objects;

public class CommandUtil {


    public static boolean check(Update update, Command command) {
        return Objects.nonNull(update)
                && Objects.nonNull(command)
                && Objects.nonNull(update.message().text())
                && update.message().text().contentEquals(command.code());
    }

}
