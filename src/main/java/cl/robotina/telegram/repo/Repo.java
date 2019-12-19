package cl.robotina.telegram.repo;

import cl.robotina.telegram.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class Repo {

    private static final String NAME_DATABASE = "indicadores";

    private static final String TABLE = "user";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ID_USER = "iduser";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_CREATION = "datecreation";
    private static final String COLUMN_UPDATE = "dateupdate";

    private Connection connect;

    public Repo() throws Exception {
        super();
        connect = initConnection();
        preparing(TABLE);

    }

    private Connection initConnection() throws Exception {
        try {
            if (this.connect == null) {
                new File("db/").mkdirs();
                return DriverManager.getConnection("jdbc:sqlite:db/" + NAME_DATABASE + ".db");
            }
            return connect;
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    private void preparing(String tableName) throws Exception {
        if (!tableExist(tableName)) {
            String sql = String.format(
                    "create table %s (%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s DATETIME NOT NULL, %s DATETIME NOT NULL)", tableName,
                    COLUMN_ID, COLUMN_ID_USER, COLUMN_USERNAME, COLUMN_STATE, COLUMN_CREATION, COLUMN_UPDATE);
            log.info(sql);
            new QueryRunner().update(connect, sql);

        }
    }

    private boolean tableExist(String tableName) throws Exception {

        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";

        try (PreparedStatement statement = connect.prepareStatement(sql)) {
            statement.setString(1, tableName);
            String ls = null;
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ls = rs.getString("name");
                }
            }
            return ls != null && !ls.isEmpty();

        } catch (SQLException e) {
            throw new Exception(e);
        }

    }

    public void close() throws Exception {
        DbUtils.close(connect);
    }


    public User getByIdUser(Long idUser) throws Exception {
        if (Objects.isNull(idUser)) {
            return null;
        }
        return new QueryRunner().query(connect, "SELECT * FROM " + TABLE + " WHERE " + COLUMN_ID_USER + "=?",
                new BeanHandler<User>(User.class), idUser);
    }

    public Integer save(User user) throws Exception {
        if (Objects.isNull(user) || Objects.isNull(user.getIdUser()) || Objects.isNull(user.getState()) || Objects.isNull(user.getUsername())) {
            return null;
        }
        if (Objects.isNull(user.getId())) {
            String insertQuery = "INSERT INTO user(" + COLUMN_ID_USER + "," + COLUMN_USERNAME + "," + COLUMN_STATE + "," + COLUMN_CREATION + "," + COLUMN_UPDATE + ") VALUES (?,?,?,?,?)";
            return new QueryRunner().update(connect, insertQuery, user.getIdUser(), user.getUsername(), user.getState(),
                    new Date(), new Date());
        } else {
            String updateQuery = "UPDATE user SET " + COLUMN_ID_USER + "=?," + COLUMN_USERNAME + "=?," + COLUMN_STATE + "=?," + COLUMN_UPDATE + "=? WHERE " + COLUMN_ID + "=?";
            return new QueryRunner().update(connect, updateQuery, user.getIdUser(), user.getUsername(), user.getState(), new Date(), user.getId());
        }

    }
}
