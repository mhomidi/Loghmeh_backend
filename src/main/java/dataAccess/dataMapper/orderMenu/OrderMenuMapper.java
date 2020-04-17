package dataAccess.dataMapper.orderMenu;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.databaseEntity.OrderMenuDAO;
import domain.databaseEntity.OrdersDAO;
import domain.entity.Menu;
import domain.entity.Order;

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
        return "INSERT INTO Menus (orderId, menuId, countFood,  isFoodParty) " +
                "VALUES(?, ?, ?, ?)";
    }

    @Override
    protected OrderMenuDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<OrderMenuDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, OrderMenuDAO orderMenuDAO) throws SQLException {
        st.setInt(1,orderMenuDAO.getOrderId());
        st.setInt(2,orderMenuDAO.getMenuId());
        st.setInt(3,orderMenuDAO.getCount());
        st.setBoolean(4,orderMenuDAO.isFoodParty());
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






}
