package dataAccess.dataMapper.orders;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.FrontEntity.MenuDTO;
import domain.FrontEntity.SingleUserOrderDTO;
import domain.databaseEntity.FoodPartyDAO;
import domain.databaseEntity.OrdersDAO;
import domain.entity.Menu;
import domain.entity.Order;
import domain.exceptions.FoodNotExist;
import domain.exceptions.NoCurrOrder;
import domain.exceptions.NotFindOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersMapper extends Mapper<OrdersDAO, String> implements IOrdersMapper {

    private static OrdersMapper instance;

    static {
        try {
            instance = new OrdersMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private OrdersMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS Orders " +
                "( orderId INT AUTO_INCREMENT,\n" +
                " username VARCHAR(50),\n" +
                " restaurantId varchar(250),\n" +
                " status INT DEFAULT 1 ,\n" +
                " calcDeliveryTime INT DEFAULT NULL ,\n" +
                " deliverPersonId varchar(250) default NULL ,\n" +
                " FOREIGN KEY(username) references Users(username) on delete cascade ,\n" +
                " FOREIGN KEY(restaurantId) references Restaurants(restaurantId) on delete cascade ,\n" +
                " FOREIGN KEY(deliverPersonId) references Deliveries(deliveryId),\n" +
                " PRIMARY KEY(orderId));\n"
        );
        //not finalizing 1
        // finding delivery 2
        // delivery 3
        // done 4
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static OrdersMapper getInstance() {
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
        return "INSERT INTO Orders (username, restaurantId) " +
                "VALUES(?, ?)";
    }

    @Override
    protected OrdersDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<OrdersDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, OrdersDAO order) throws SQLException {
        st.setString(1,order.getUsername());
        st.setString(2,order.getRestaurantId());
    }


    public boolean insert(OrdersDAO order) throws SQLException {
        boolean result = true;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(getInsertStatement());
        fillInsertValues(preparedStatement, order);
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



    public boolean checkUserStartChoosingFoodInCurrOrder(String username)
            throws  SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT *\n" +
                        "FROM Orders \n" +
                        "where username=? AND status=?");
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2,1); // not finalizing
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        if(!resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            con.close();
            return true;   // when there is no tuple so it means start order
        }
        else{
            resultSet.close();
            preparedStatement.close();
            con.close();
            return  false;
        }
    }


    public String getRestaurantIdForCurrOrderOfUser(String username)
            throws  SQLException , NoCurrOrder {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT *\n" +
                        "FROM Orders \n" +
                        "where username=? AND status=?");
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2,1); // not finalizing
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        if(!resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            con.close();
            throw  new NoCurrOrder();
        }
        else{
            String restaurantId = resultSet.getString(3);
            resultSet.close();
            preparedStatement.close();
            con.close();
            return restaurantId;
        }
    }


    public String getRestaurantIdForOrderOfUser(int orderId)
            throws  SQLException , NotFindOrder {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT *\n" +
                        "FROM Orders \n" +
                        "where orderId=?");
        preparedStatement.setInt(1,orderId);
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        if(!resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            con.close();
            throw new NotFindOrder();
        }
        else{
            String restaurantId = resultSet.getString(3);
            resultSet.close();
            preparedStatement.close();
            con.close();
            return restaurantId;
        }
    }


    public int findOrderIdOfUserCurrOrder(String username)
            throws  SQLException , NoCurrOrder {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                "SELECT *\n" +
                        "FROM Orders \n" +
                        "where username=? AND status=?");
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2,1); // not finalizing
        ResultSet resultSet;
        resultSet =preparedStatement.executeQuery();
        if(!resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            con.close();
            throw  new NoCurrOrder();
        }
        else{
            int orderId = resultSet.getInt(1);
            resultSet.close();
            preparedStatement.close();
            con.close();
            return orderId;
        }
    }


    public void deleteOrderForUser(int orderId)
            throws SQLException{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement("" +
                "DELETE FROM Orders " +
                "WHERE orderId=?");
        preparedStatement.setInt(1,orderId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();
    }


    public void changeStatusOfUserOrderToFindingDelivery(int orderId) throws SQLException{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement("" +
                "UPDATE Orders SET status=? WHERE orderId=?");
        preparedStatement.setInt(1,2);
        preparedStatement.setInt(2,orderId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();
    }


    public void addCalcDeliveryTimeToOrder(int orderId , int calc){
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("" +
                    "UPDATE Orders SET calcDeliveryTime=? WHERE orderId=?");
            preparedStatement.setInt(1,calc);
            preparedStatement.setInt(2,orderId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
        }catch (SQLException ignored){

        }
    }

    public void updateOrderAddDeliveryIdChangeStatus(int orderId, String deliveryId){
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("" +
                    "UPDATE Orders SET deliverPersonId=?, status=? WHERE orderId=?");
            preparedStatement.setString(1,deliveryId);
            preparedStatement.setInt(2,3);
            preparedStatement.setInt(3,orderId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
        }catch (SQLException ignored){

        }
    }


    public ArrayList<Integer> getAllUndeliveredOrders() {
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(
                    "SELECT distinct Orders.orderId\n" +
                            "FROM Orders\n" +
                            "WHERE status=?");
            preparedStatement.setInt(1, 2);
            ResultSet resultSet;
            resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> res = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                res.add(id);
            }
            resultSet.close();
            preparedStatement.close();
            con.close();
            return res;
        } catch (SQLException e) {
            ArrayList<Integer> err = new ArrayList<>();
            return err;
        }
    }




    public ArrayList<SingleUserOrderDTO> getAllUserOrders(String username)throws SQLException{
        ArrayList<SingleUserOrderDTO> orders = new ArrayList<SingleUserOrderDTO>();
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(
                " SELECT distinct orderId, Orders.restaurantId, Restaurants.restaurantName," +
                        " Orders.status, Orders.calcDeliveryTime,  Orders.deliverPersonId" +
                        " FROM Orders , Restaurants\n" +
                        "where Orders.username =? and Restaurants.restaurantId = Orders.restaurantId");
        preparedStatement.setString(1,username);
        ResultSet resultSet;
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int orderId = resultSet.getInt(1);
            String restaurantId = resultSet.getString(2);
            String restaurantName = resultSet.getString(3);
            int status = resultSet.getInt(4);
            int calcDeliveryTime = resultSet.getInt(5);
            String  deliverPersonId = resultSet.getString(6);
            orders.add(new SingleUserOrderDTO(orderId, restaurantId, restaurantName, status,
                    calcDeliveryTime, deliverPersonId));
        }
        resultSet.close();
        preparedStatement.close();
        con.close();
        return orders;
    }
}
