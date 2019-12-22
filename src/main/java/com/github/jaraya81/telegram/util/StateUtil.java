package com.github.jaraya81.telegram.util;

import com.github.jaraya81.telegram.enums.State;
import com.github.jaraya81.telegram.exception.TelegramException;
import com.github.jaraya81.telegram.model.User;

import java.util.Objects;

public class StateUtil {

    public static boolean is(User user, State state) throws TelegramException {
        if (!User.exist(user) || Objects.isNull(state)) {
            throw new TelegramException("");
        }
        return user.getState().contentEquals(state.name());
    }

    public static boolean isBanned(User user) throws TelegramException {
        return is(user, State.BANNED);
    }
}
