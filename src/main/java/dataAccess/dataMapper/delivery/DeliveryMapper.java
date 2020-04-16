package dataAccess.dataMapper.delivery;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.entity.Delivery;
import domain.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryMapper extends Mapper<Delivery, String> implements IDeliveryMapper {

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
        return null;
    }

    @Override
    protected Delivery convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<Delivery> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Delivery user) throws SQLException {

    }






}
