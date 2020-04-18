package dataAccess.dataMapper.orderMenu;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.databaseEntity.OrderMenuDAO;
import domain.databaseEntity.OrdersDAO;
import domain.entity.Menu;
import domain.entity.Order;
import domain.exceptions.NoCurrOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderMenuMapper extends Mapper<OrderMenuDAO, String> implements IOrderMenuMapper {

    private static OrderMenuMapper instance;

    static {
        try {
            instance = new OrderMenuMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private OrderMenuMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS OrderMenu " +
                "( id INT AUTO_INCREMENT,\n" +
                "  orderId INT,\n" +
                "  menuId INT,\n" +
                "  foodName VARCHAR(250),\n" +
                "  price DOUBLE ,\n" +
                "  countFood INT ,\n" +
                "  isFoodParty boolean,\n" +
                "  FOREIGN KEY(orderId) references Orders(orderId) ON DELETE CASCADE,\n" +
                "  FOREIGN KEY(menuId) references Menus(menuId) ON DELETE CASCADE,\n" +
                "  PRIMARY KEY(id));\n"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static OrderMenuMapper getInstance() {
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
        return "INSERT INTO OrderMenu (orderId, menuId, foodName , price, countFood,  isFoodParty) " +
                "VALUES(?, ?, ?, ?,?,?)";
    }

    @Override
    protected OrderMenuDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        int orderId = rs.getInt(2);
        int menuId = rs.getInt(3);
        String foodName = rs.getString(4);
        Double price = rs.getDouble(5);
        int countFood = rs.getInt(6);
        boolean isFoodParty = rs.getBoolean(7);
        return new OrderMenuDAO(orderId, menuId,foodName, price, countFood, isFoodParty);
    }

    @Override
    protected ArrayList<OrderMenuDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, OrderMenuDAO orderMenuDAO) throws SQLException {
        st.setInt(1,orderMenuDAO.getOrderId());
        st.setInt(2,orderMenuDAO.getMenuId());
        st.setString(3,orderMenuDAO.getFoodName());
        st.setDouble(4,orderMenuDAO.getPrice());
        st.setInt(5,orderMenuDAO.getCount());
        st.setBoolean(6,orderMenuDAO.isFoodParty());
    }


    public boolean insert(OrderMenuDAO orderMenuDAO) throws SQLException {
        boolean result = true;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(getInsertStatement());
        fillInsertValues(preparedStatement, orderMenuDAO);
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


    public void InsertOrUpdateCountFood(int orderId, int menuId,String foodName , Double price, int count, boolean isFoodParty)
    throws SQLException{
        System.out.println("user want to update or insert new food to order");
        OrderMenuDAO orderMenuDAO = checkFoodExistInOrder(orderId, menuId , isFoodParty);
        System.out.println(orderMenuDAO);
        if (orderMenuDAO == null){
            insert(new OrderMenuDAO(orderId, menuId, foodName, price,  count, isFoodParty));
        }
        else {
            updateCountFoodInOrder(orderId, menuId, isFoodParty, orderMenuDAO.getCount() + count);
        }
    }




    public OrderMenuDAO checkFoodExistInOrder(int orderId, int menuId , boolean isFoodParty) throws SQLException{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT * " +
                        " FROM OrderMenu" +
                        " WHERE orderId=? AND menuId=? AND isFoodParty=?");
        preparedStatement.setInt(1, orderId);
        preparedStatement.setInt(2,menuId);
        preparedStatement.setBoolean(3,isFoodParty);
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        if(!resultSet.next()) {
            System.out.println("not find food");
            resultSet.close();
            preparedStatement.close();
            con.close();
            return null;
        }
        else{
            System.out.println("find food");
            OrderMenuDAO orderMenuDAO = convertResultSetToDomainModel(resultSet);
            resultSet.close();
            preparedStatement.close();
            con.close();
            return orderMenuDAO;
        }
    }




    public void updateCountFoodInOrder(int orderId, int menuId , boolean isFoodParty , int new_count)
        throws SQLException{
        System.out.println("update...");
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("" +
                    " UPDATE OrderMenu" +
                    " SET countFood=?" +
                    " WHERE orderId=? AND menuId=? AND isFoodParty=?");
            preparedStatement.setInt(1,new_count);
            preparedStatement.setInt(2,orderId);
            preparedStatement.setInt(3,menuId);
            preparedStatement.setBoolean(4,isFoodParty);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
    }
}
