package dataAccess.dataMapper.delivery;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.databaseEntity.DeliveryDAO;
import domain.databaseEntity.MenuDAO;
import domain.entity.Delivery;
import domain.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryMapper extends Mapper<DeliveryDAO, String> implements IDeliveryMapper {

    private static DeliveryMapper instance;

    static {
        try {
            instance = new DeliveryMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DeliveryMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS Deliveries " +
                "(  deliveryId VARCHAR(250),\n" +
                "   velocity DOUBLE ,\n" +
                "   location_X DOUBLE ,\n" +
                "   location_Y DOUBLE ,\n" +
                "   active BOOLEAN ,\n" +
                "   PRIMARY KEY(deliveryId));\n"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static DeliveryMapper getInstance() {
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
        return "INSERT INTO Deliveries (deliveryId,  velocity, location_X, location_Y , active) " +
                "VALUES(?, ?, ?, ? , ?)";
    }


    @Override
    protected DeliveryDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<DeliveryDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, DeliveryDAO delivery) throws SQLException {
        st.setString(1, delivery.getId());
        st.setDouble(2, delivery.getVelocity());
        st.setDouble(3, delivery.getLocation_X());
        st.setDouble(4, delivery.getLocation_Y());
        st.setBoolean(5, true);
    }


    public void makeAllDeliveryInActive(){
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Deliveries SET active=?");
            preparedStatement.setBoolean(1,false);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
        }catch (SQLException ignored){
        }
    }

    public void ActivateDeliveryWithId(String id){
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE Deliveries SET active=? WHERE deliveryId=?");
            preparedStatement.setBoolean(1,true);
            preparedStatement.setString(2,id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
        }catch (SQLException ignored){
        }
    }



    public boolean insert(DeliveryDAO delivery) throws SQLException {
        boolean result = true;
        ActivateDeliveryWithId(delivery.getId());
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(getInsertStatement());
        fillInsertValues(preparedStatement, delivery);
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
