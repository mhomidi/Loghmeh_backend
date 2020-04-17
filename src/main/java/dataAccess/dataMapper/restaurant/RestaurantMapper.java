package dataAccess.dataMapper.restaurant;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.FrontEntity.FoodPartyDTO;
import domain.FrontEntity.MenuDTO;
import domain.FrontEntity.RestaurantMenuDTO;
import domain.databaseEntity.RestaurantDAO;
import domain.databaseEntity.UserDAO;
import domain.entity.Menu;
import domain.exceptions.RestaurantNotAvailable;
import domain.exceptions.RestaurantNotFound;
import domain.exceptions.UserNotFound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RestaurantMapper extends Mapper<RestaurantDAO, String> implements IRestaurantMapper {

    private static RestaurantMapper instance;

    static {
        try {
            instance = new RestaurantMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private RestaurantMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS Restaurants " +
                "(restaurantId varchar(250),\n" +
                " restaurantName text CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                " logoUrl text,\n" +
                " location_X DOUBLE ,\n" +
                " location_Y DOUBLE ,\n" +
                " PRIMARY KEY(restaurantId));\n"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static RestaurantMapper getInstance() {
        return instance;
    }

    @Override
    protected String getFindAllStatement() {
        return null;
    }



    @Override
    protected String getFindStatement() {
        return "SELECT *" +
                " FROM Restaurants" +
                " WHERE restaurantId = ?";
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT INTO Restaurants (restaurantId, restaurantName,  logoUrl, location_X, location_Y) " +
                "VALUES(?, ?, ?, ?, ?)";
    }

    @Override
    protected RestaurantDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        String restaurantId = rs.getString(1);
        String restaurantName = rs.getString(2);
        String logo = rs.getString(3);
        Double loc_x = rs.getDouble(4);
        Double loc_y = rs.getDouble(5);
        return new RestaurantDAO(restaurantId, restaurantName, logo, loc_x, loc_y);
    }

    @Override
    protected ArrayList<RestaurantDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, RestaurantDAO restaurant) throws SQLException {
        st.setString(1, restaurant.getRestaurantId());
        st.setString(2, restaurant.getRestaurantName());
        st.setString(3 , restaurant.getLogoUrl());
        st.setDouble(4, restaurant.getLocation_X());
        st.setDouble(5,restaurant.getLocation_Y());
    }


    public boolean insert(RestaurantDAO restaurant) throws SQLException {
        boolean result = true;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(getInsertStatement());
        fillInsertValues(preparedStatement, restaurant);
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


    public ArrayList<RestaurantDAO> findAvailableRestaurants() throws SQLException{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement("SELECT DISTINCT *" +
                "FROM Restaurants " +
                "WHERE location_X*location_X + location_Y*location_Y<=28900");
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        ArrayList<RestaurantDAO> result = new ArrayList<>();
        while(resultSet.next())
        {
            result.add(convertResultSetToDomainModel(resultSet));
        }
        preparedStatement.close();
        con.close();
        return result;
    }


    public ArrayList<FoodPartyDTO> findAvailableFoodParties() throws SQLException{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT R.restaurantId, R.restaurantName, R.logoUrl,\n" +
                "       M.menuId, M.foodName, M.description, M.popularity, M.price, M.foodUrlImage,\n" +
                "       FPM.newPrice, FPM.FoodCount\n" +
                "From FoodPartyMenus FPM\n" +
                "join Menus M on FPM.menuId = M.menuId\n" +
                "join Restaurants R on M.restaurantId = R.restaurantId\n" +
                "where FPM.available=true AND R.location_X*R.location_X + R.location_Y * R.location_Y<=28900;");
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        ArrayList<FoodPartyDTO> result = new ArrayList<>();
        while(resultSet.next())
        {
            String restaurantId = resultSet.getString(1);
            String restaurantName = resultSet.getString(2);
            String restaurantLogo = resultSet.getString(3);
            int menuId = resultSet.getInt(4);
            String foodName = resultSet.getString(5);
            String foodDescription = resultSet.getString(6);
            Double foodPopularity = resultSet.getDouble(7);
            Double foodOldPrice = resultSet.getDouble(8);
            String foodUrl = resultSet.getString(9);
            Double foodNewPrice = resultSet.getDouble(10);
            int foodCount = resultSet.getInt(11);
            FoodPartyDTO newFood = new FoodPartyDTO(restaurantId, restaurantName, restaurantLogo,
                    menuId, foodName, foodDescription, foodNewPrice, foodOldPrice,
                    foodUrl, foodPopularity, foodCount);
            result.add(newFood);
        }
        resultSet.close();
        preparedStatement.close();
        con.close();
        return result;
    }


    public RestaurantMenuDTO getRestaurantWithMenusById(String restaurantId)
            throws RestaurantNotFound, RestaurantNotAvailable, SQLException {
        RestaurantDAO restaurant;
        restaurant = find(restaurantId);
        if(restaurant == null) {
            throw new RestaurantNotFound();
        }
        if(restaurant.getLocation_X()*restaurant.getLocation_X() +
                restaurant.getLocation_Y()*restaurant.getLocation_Y() > 28900){
            throw new RestaurantNotAvailable();
        }
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT *\n" +
                        "FROM Menus M\n" +
                        "where M.restaurantId=? AND M.menuId not in (\n" +
                        "    SELECT FPM.menuId FROM FoodPartyMenus FPM\n" +
                        "    );");
        preparedStatement.setString(1,restaurantId);
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        RestaurantMenuDTO restaurantMenuDTO = new RestaurantMenuDTO(restaurantId,
                restaurant.getRestaurantName(), restaurant.getLogoUrl());
        ArrayList<MenuDTO> menuDTOS = new ArrayList<>();
        while(resultSet.next()) {
            int menuId = resultSet.getInt(1);
            String foodName = resultSet.getString(2);
            String foodDescription = resultSet.getString(3);
            Double foodPopularity = resultSet.getDouble(4);
            Double foodPrice = resultSet.getDouble(5);
            String foodUrl = resultSet.getString(6);
            menuDTOS.add(new MenuDTO(menuId, foodName, foodPrice, foodPopularity, foodDescription, foodUrl, restaurantId));
        }
        resultSet.close();
        preparedStatement.close();
        con.close();
        restaurantMenuDTO.setMenus(menuDTOS);
        return restaurantMenuDTO;
    }


}
