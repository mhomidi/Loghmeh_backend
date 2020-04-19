package dataAccess.dataMapper.restaurant;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.FrontEntity.FoodPartyDTO;
import domain.FrontEntity.MenuDTO;
import domain.FrontEntity.RestaurantMenuDTO;
import domain.databaseEntity.DeliveryDAO;
import domain.databaseEntity.RestaurantDAO;
import domain.databaseEntity.UserDAO;
import domain.entity.Menu;
import domain.exceptions.*;

import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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


    public ArrayList<RestaurantDAO> findAvailableRestaurants(int pageNumber , int size,
                                                             String searchFoodKey, String searchRestaurantKey) throws SQLException{
        Connection con = ConnectionPool.getConnection();
        if (pageNumber == -1){
            PreparedStatement preparedStatement = con.prepareStatement("SELECT DISTINCT * " +
                    "FROM Restaurants  " +
                    "WHERE location_X*location_X + location_Y*location_Y<=28900 " +
                    "AND restaurantName LIKE  ?" +
                    "UNION " +
                    "SELECT R.restaurantId, R.restaurantName , R.logoUrl , R.location_X , R.location_Y " +
                    "FROM Menus M , Restaurants R " +
                    "WHERE M.restaurantId = R.restaurantId  AND M.foodName LIKE ? " +
                    "AND M.menuId not in (SELECT FPM.menuId FROM FoodPartyMenus FPM);");

            preparedStatement.setString(1,"%" + searchRestaurantKey + "%");
            preparedStatement.setString(2,"%" + searchFoodKey + "%");
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
        else {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT DISTINCT * " +
                    "FROM Restaurants  " +
                    "WHERE location_X*location_X + location_Y*location_Y<=28900 " +
                    "AND restaurantName LIKE  ?" +
                    "UNION " +
                    "SELECT R.restaurantId, R.restaurantName , R.logoUrl , R.location_X , R.location_Y " +
                    "FROM Menus M , Restaurants R " +
                    "WHERE M.restaurantId = R.restaurantId  AND M.foodName LIKE ? " +
                    "AND M.menuId not in (SELECT FPM.menuId FROM FoodPartyMenus FPM) " +
                    "LIMIT ?,?");

            preparedStatement.setString(1,"%" + searchRestaurantKey + "%");
            preparedStatement.setString(2,"%" + searchFoodKey + "%");
            int offset = size * (pageNumber - 1);
            preparedStatement.setInt(3, offset);
            preparedStatement.setInt(4, size);
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


    public boolean canChooseFoodParty(String restaurantId , int menuId, String foodName , int foodCount)
        throws SQLException, FoodNotInFoodParty, TimeValidationErrorFoodParty, CountValidationErrorFoodParty{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT M.menuId, M.foodName, R.restaurantName, R.restaurantId, M.price as oldPrice," +
                        " FPM.newPrice , FPM.FoodCount , FPM.available\n" +
                        "From FoodPartyMenus FPM " +
                        "join Menus M on FPM.menuId = M.menuId " +
                        "join Restaurants R on M.restaurantId = R.restaurantId " +
                        "WHERE R.restaurantId=? AND M.menuId=? AND M.foodName=?");
        preparedStatement.setString(1,restaurantId);
        preparedStatement.setInt(2,menuId);
        preparedStatement.setString(3, foodName);
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        if(!resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            con.close();
            throw new FoodNotInFoodParty();
        }
        else{
            Boolean available = resultSet.getBoolean(8);
            if(!available){
                resultSet.close();
                preparedStatement.close();
                con.close();
                throw new TimeValidationErrorFoodParty();
            }
            int count = resultSet.getInt(7);
            if (foodCount>count){
                resultSet.close();
                preparedStatement.close();
                con.close();
                throw new CountValidationErrorFoodParty();
            }
            return true;
        }

    }




    public boolean CountAndTimeValidationForFoodPartyOfUserOrder(int orderId)
            throws SQLException , TimeValidationErrorFoodParty , CountValidationErrorFoodParty{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT foodPartyUserChoose.menuId, foodPartyUserChoose.countFood as userCountFood ," +
                        " FoodPartyMenus.available , FoodPartyMenus.FoodCount\n" +
                        "FROM\n" +
                        "(SELECT OrderMenu.menuId , OrderMenu.foodName , OrderMenu.countFood\n" +
                        "from OrderMenu\n" +
                        "Where OrderMenu.orderId = ? and OrderMenu.isFoodParty=?) as foodPartyUserChoose join FoodPartyMenus\n" +
                        "on FoodPartyMenus.menuId = foodPartyUserChoose.menuId");
        preparedStatement.setInt(1,orderId);
        preparedStatement.setInt(2,1);
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        while(resultSet.next()) {
            int menuId = resultSet.getInt(1);
            int userCountFood = resultSet.getInt(2);
            boolean available = resultSet.getBoolean(3);
            int foodCount = resultSet.getInt(4);
            if (!available){
                resultSet.close();
                preparedStatement.close();
                con.close();
                System.out.println("time validation error for " + Integer.toString(menuId));
                throw new TimeValidationErrorFoodParty();
            }
            if (userCountFood > foodCount){
                resultSet.close();
                preparedStatement.close();
                con.close();
                System.out.println("count validation error for " + Integer.toString(menuId));
                throw new CountValidationErrorFoodParty();
            }
        }
        resultSet.close();
        preparedStatement.close();
        con.close();
        return true;
    }


    public HashMap<Integer, Integer> getFoodPartiesWithCountInUserOrder(int orderId)
    throws SQLException{
        HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT menuId ,countFood " +
                        "FROM OrderMenu\n" +
                        "Where orderId = ? and isFoodParty=?");
        preparedStatement.setInt(1,orderId);
        preparedStatement.setBoolean(2,true);
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        while(resultSet.next()) {
            int menuId = resultSet.getInt(1);
            int count = resultSet.getInt(2);
            res.put(menuId, count);
        }
        resultSet.close();
        preparedStatement.close();
        con.close();
        return res;
    }


}
