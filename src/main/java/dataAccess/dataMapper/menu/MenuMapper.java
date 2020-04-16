package dataAccess.dataMapper.menu;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.entity.Menu;
import domain.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuMapper extends Mapper<Menu, String> implements IMenuMapper {

    private static MenuMapper instance;

    static {
        try {
            instance = new MenuMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private MenuMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS MENUS " +
                "( menuId INT AUTO_INCREMENT,\n" +
                "  foodName text CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "  description text CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "  popularity DOUBLE ,\n" +
                "  price DOUBLE ,\n" +
                "  foodUrlImage text,\n" +
                "  restaurantId varchar(250),\n" +
                "  FOREIGN KEY(restaurantId) references Restaurants(restaurantId) ON DELETE CASCADE,\n" +
                "  PRIMARY KEY(menuId));\n"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static MenuMapper getInstance() {
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
    protected Menu convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<Menu> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Menu user) throws SQLException {

    }






}
