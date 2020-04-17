package dataAccess.dataMapper.restaurant;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.databaseEntity.RestaurantDAO;
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
      return null;
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




}
