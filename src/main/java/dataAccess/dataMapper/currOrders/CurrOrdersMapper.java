package dataAccess.dataMapper.currOrders;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CurrOrdersMapper extends Mapper<Order, String> implements ICurrOrdersMapper {

    private static CurrOrdersMapper instance;

    static {
        try {
            instance = new CurrOrdersMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private CurrOrdersMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS CurrOrders " +
                "(username VARCHAR(20),\n" +
                " orderId INT ,\n" +
                " FOREIGN KEY(username) references Users(username),\n" +
                " FOREIGN KEY(orderId) references Orders(orderId));"
        );
        //not finalizing 1
        // finding delivery 2
        // delivery 3
        // done 4
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static CurrOrdersMapper getInstance() {
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
    protected Order convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<Order> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Order order) throws SQLException {

    }






}
