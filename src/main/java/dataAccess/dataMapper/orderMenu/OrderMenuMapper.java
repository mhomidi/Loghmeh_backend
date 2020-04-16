package dataAccess.dataMapper.orderMenu;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.entity.Menu;
import domain.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderMenuMapper extends Mapper<Order, String> implements IOrderMenuMapper {

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
