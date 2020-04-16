package dataAccess.dataMapper.user;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.entity.User;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserMapper extends Mapper<User, String> implements IUserMapper {

    private static UserMapper instance;

    static {
        try {
            instance = new UserMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private UserMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS Users (username CHAR(20),\n" +
                "    firstname CHAR(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "    lastname VARCHAR(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "    password VARCHAR(250),\n" +
                "    email VARCHAR(250),\n" +
                "    phone VARCHAR(20),\n" +
                "    credit DOUBLE ,\n" +
                "    PRIMARY KEY(username));\n"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static UserMapper getInstance() {
        return instance;
    }

    @Override
    protected String getFindAllStatement() {
        return null;
    }



    @Override
    protected String getFindStatement() {
      return null;
    }

    @Override
    protected String getInsertStatement() {
        return null;
    }

    @Override
    protected User convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<User> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, User user) throws SQLException {

    }






}
