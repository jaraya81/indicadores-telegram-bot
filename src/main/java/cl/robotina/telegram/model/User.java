package cl.robotina.telegram.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@ToString
public class User implements Serializable {

    private Long id;
    private Long idUser;
    private String username;
    private String state;
    private Date datecreation;
    private Date dateupdate;

    @Tolerate
    public User() {
        super();
    }
}
