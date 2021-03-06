package com.github.jaraya81.telegram.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Data
@Builder
@ToString
public class User implements Serializable {

    private Long id;
    private Long idUser;
    private String username;
    private String state;
    private Timestamp datecreation;
    private Timestamp dateupdate;

    @Tolerate
    public User() {
        super();
    }

    public static boolean exist(User user) {
        return Objects.nonNull(user);
    }
}
