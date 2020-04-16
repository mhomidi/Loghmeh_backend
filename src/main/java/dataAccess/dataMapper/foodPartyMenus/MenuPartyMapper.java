package dataAccess.dataMapper.foodPartyMenus;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.entity.Menu;
import domain.entity.MenuParty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuPartyMapper extends Mapper<MenuParty, String> implements IMenuPartyMapper {

    private static MenuPartyMapper instance;

    static {
        try {
            instance = new MenuPartyMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private MenuPartyMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS FoodPartyMenus " +
                "( menuId INT AUTO_INCREMENT,\n" +
                "  foodName text CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "  description text CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "  popularity DOUBLE ,\n" +
                "  newPrice DOUBLE ,\n" +
                "  oldPrice DOUBLE ,\n" +
                "  FoodCount INT ,\n" +
                "  foodUrlImage text,\n" +
                "  restaurantId varchar(250),\n" +
                "  FOREIGN KEY(restaurantId) references Restaurants(restaurantId) ON DELETE CASCADE,\n" +
                "  PRIMARY KEY(menuId));\n"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static MenuPartyMapper getInstance() {
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
    protected MenuParty convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<MenuParty> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, MenuParty menuParty) throws SQLException {

    }






}
