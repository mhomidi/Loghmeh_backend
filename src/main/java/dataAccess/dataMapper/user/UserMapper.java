package dataAccess.dataMapper.user;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.FrontEntity.BuyBasketDTO;
import domain.FrontEntity.FoodInBasketDTO;
import domain.FrontEntity.MenuDTO;
import domain.FrontEntity.RestaurantMenuDTO;
import domain.databaseEntity.UserDAO;
import domain.exceptions.NoCurrOrder;
import domain.exceptions.UserAlreadyExists;
import domain.exceptions.UserNotFound;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserMapper extends Mapper<UserDAO, String> implements IUserMapper {

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
                "    firstname VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "    lastname VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "    password text,\n" +
                "    email VARCHAR(100),\n" +
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
    protected String getInsertStatement() {
        return "INSERT INTO Users (username, firstname, lastname, password, email, phone, credit) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
    }


    @Override
    protected void fillInsertValues(PreparedStatement st, UserDAO user) throws SQLException {
        st.setString(1, user.getUsername());
        st.setString(2, user.getFirstName());
        st.setString(3, user.getLastName());
        st.setString(4, user.getPassword());
        st.setString(5, user.getEmail());
        st.setString(6,user.getPhone());
        st.setDouble(7,0.0);
    }


    @Override
    public String getValidateUserStatement() {
        return "SELECT * " +
                "FROM Users U " +
                "WHERE U.username = ? " +
                "AND U.password = ? ";
    }

    @Override
    protected String getFindStatement() {
        return "SELECT *" +
                " FROM Users" +
                " WHERE username = ?";
    }


    @Override
    public void fillValidateUserStatement(PreparedStatement st, String username, String pass) throws SQLException {
        st.setString(1, username);
        st.setString(2, pass);
    }

    @Override
    public boolean validateUser(String username, String password) throws SQLException {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     getValidateUserStatement())
        ) {
            fillValidateUserStatement(stmt, username, password);
            ResultSet resultSet;
            resultSet = stmt.executeQuery();
            boolean valid = false;
            if(resultSet.next())
                valid = true;
            resultSet.close();
            stmt.close();
            con.close();
            return valid;
        }
    }





    @Override
    protected String getFindAllStatement() {
        return null;
    }






    @Override
    protected UserDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new UserDAO (
                rs.getString(2),
                rs.getString(3),
                rs.getString(1),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getDouble(7)
        );
    }

    @Override
    protected ArrayList<UserDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }


    @Override
    public boolean insert(UserDAO user) throws SQLException {
        boolean result = true;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(getInsertStatement());
        fillInsertValues(preparedStatement, user);
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


    public void  registerUser(UserDAO user) throws UserAlreadyExists,SQLException{
        if(find(user.getUsername()) != null) {
            throw new UserAlreadyExists();
        }
        this.insert(user);
    }


    public UserDAO getUserById(String username) throws UserNotFound, SQLException {
        UserDAO user;
        user = find(username);
        if(user != null)
            return user;
        else
            throw new UserNotFound();
    }

    public void updateUserCredit(String username , Double new_credit) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement("UPDATE Users SET credit=? WHERE username=?");
        preparedStatement.setDouble(1, new_credit);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();
    }


    public BuyBasketDTO getUserCurrBuyBasket(String username)throws SQLException{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT Orders.orderId , OrderMenu.menuId , OrderMenu.foodName , OrderMenu.price , OrderMenu.countFood " +
                        "FROM Orders , OrderMenu " +
                        "WHERE Orders.orderId = OrderMenu.orderId" +
                        " AND Orders.status = ? and Orders.username = ?");
        preparedStatement.setInt(1,1);
        preparedStatement.setString(2,username);
        ResultSet resultSet;
        resultSet = preparedStatement.executeQuery();
        BuyBasketDTO buyBasketDTO = new BuyBasketDTO(username);
        ArrayList<FoodInBasketDTO> foodInBasketDTOS = new ArrayList<>();
        while(resultSet.next()) {
            int orderId = resultSet.getInt(1);
            int menuId = resultSet.getInt(2);
            String foodName = resultSet.getString(3);
            Double price = resultSet.getDouble(4);
            int count = resultSet.getInt(5);
            foodInBasketDTOS.add(new FoodInBasketDTO(menuId, foodName, count, price));
            buyBasketDTO.setOrderId(orderId);
        }
        buyBasketDTO.setFoods(foodInBasketDTOS);
        resultSet.close();
        preparedStatement.close();
        con.close();
        return buyBasketDTO;
    }


    public Double getUserCredit(String username) throws SQLException , UserNotFound{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT credit\n" +
                        "FROM Users \n" +
                        "where username=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        if(!resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            con.close();
            throw new UserNotFound();
        }
        else{
            Double credit = resultSet.getDouble(1);
            resultSet.close();
            preparedStatement.close();
            con.close();
            return credit;
        }
    }

}
