package dataAccess.dataMapper.orders;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.databaseEntity.FoodPartyDAO;
import domain.databaseEntity.OrdersDAO;
import domain.entity.Menu;
import domain.entity.Order;

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
                " status INT DEFAULT NULL ,\n" +
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
        return "INSERT INTO Orders (username, restaurantId, status,deliverPersonId) " +
                "VALUES(?, ?, ?, ?)";
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







}
