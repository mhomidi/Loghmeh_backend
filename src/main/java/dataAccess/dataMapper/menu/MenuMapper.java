package dataAccess.dataMapper.menu;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.FrontEntity.MenuDTO;
import domain.FrontEntity.RestaurantMenuDTO;
import domain.databaseEntity.MenuDAO;
import domain.databaseEntity.RestaurantDAO;
import domain.exceptions.FoodNotExist;
import domain.exceptions.RestaurantNotAvailable;
import domain.exceptions.RestaurantNotFound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuMapper extends Mapper<MenuDAO, String> implements IMenuMapper {

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
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS Menus " +
                "( menuId INT AUTO_INCREMENT,\n" +
                "  foodName VARCHAR(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "  description text CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "  popularity DOUBLE ,\n" +
                "  price DOUBLE ,\n" +
                "  foodUrlImage text,\n" +
                "  restaurantId varchar(250),\n" +
                "  constraint menu_uniqueness UNIQUE (foodName, restaurantId),\n" +
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
        return "INSERT INTO Menus (foodName, description, popularity, price, foodUrlImage, restaurantId) " +
                "VALUES(?, ?, ?, ?, ? , ?)";
    }

    @Override
    protected MenuDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<MenuDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, MenuDAO menu) throws SQLException {
        st.setString(1, menu.getFoodName());
        st.setString(2,menu.getDescription());
        st.setDouble(3,menu.getPopularity());
        st.setDouble(4, menu.getPrice());
        st.setString(5, menu.getUrlImage());
        st.setString(6,menu.getRestaurantId());

    }


    public boolean insert(MenuDAO menu) throws SQLException {
        boolean result = true;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(getInsertStatement());
        fillInsertValues(preparedStatement, menu);
        try {
            result &= preparedStatement.execute();
        } catch (Exception e) {
            preparedStatement.close();
            con.close();
            e.printStackTrace();
            return false;
        }
        preparedStatement.close();
        con.close();
        return result;
    }


    public int getMenuId(String foodName, String restaurantId)throws SQLException {
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(
                    "SELECT M.MenuId " +
                            "FROM Menus M " +
                            "WHERE M.restaurantId=? AND M.foodName=?");
            preparedStatement.setString(1, restaurantId);
            preparedStatement.setString(2, foodName);
            ResultSet resultSet;

            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                con.close();
                return -1;
            }
            int menuId = resultSet.getInt(1);
            resultSet.close();
            preparedStatement.close();
            con.close();
            return menuId;
        } catch (SQLException ex) {
            System.out.println("error in MenuMapper.getMenuId");
            throw ex;
        }
    }



    public MenuDTO findMenuInRestaurantWithMenuNameAndMenuId(String restaurantId , String foodName, int menuId)
            throws  SQLException , FoodNotExist {

        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT *\n" +
                        "FROM Menus M\n" +
                        "where M.restaurantId=? AND M.menuId=? AND M.foodName=? AND M.menuId not in (\n" +
                        "    SELECT FPM.menuId FROM FoodPartyMenus FPM\n" +
                        "    );");
        preparedStatement.setString(1,restaurantId);
        preparedStatement.setInt(2,menuId);
        preparedStatement.setString(3,foodName);
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        if(!resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            con.close();
            throw new FoodNotExist();
        }
        else{
            MenuDTO findMenu;
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String description = resultSet.getString(3);
            Double popularity = resultSet.getDouble(4);
            Double price = resultSet.getDouble(5);
            String foodUrl = resultSet.getString(6);
            findMenu = new MenuDTO(id, name, price, popularity, description, foodUrl, restaurantId);
            resultSet.close();
            preparedStatement.close();
            con.close();
            return  findMenu;
        }
    }
}
