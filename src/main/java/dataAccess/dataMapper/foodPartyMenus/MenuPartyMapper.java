package dataAccess.dataMapper.foodPartyMenus;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.databaseEntity.DeliveryDAO;
import domain.databaseEntity.FoodPartyDAO;
import domain.entity.Menu;
import domain.entity.MenuParty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuPartyMapper extends Mapper<FoodPartyDAO, String> implements IMenuPartyMapper {

    private static MenuPartyMapper instance;

    static {
        try {
            instance = new MenuPartyMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private MenuPartyMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS FoodPartyMenus " +
                "( menuId INT ,\n" +
                "  newPrice DOUBLE ,\n" +
                "  FoodCount INT ,\n" +
                "  available BOOLEAN ,\n" +
                "  FOREIGN KEY(menuId) references Menus(menuId) ON DELETE CASCADE,\n" +
                "  PRIMARY KEY(menuId));\n"
        );
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }
    public static MenuPartyMapper getInstance() {
        return instance;
    }

    @Override
    protected String getFindAllStatement() {
        return null;
    }


    public void makeAllFoodPartyUnavailable(){
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE FoodPartyMenus SET available=?");
            preparedStatement.setBoolean(1,false);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
        }catch (SQLException ignored){
        }
    }


    @Override
    protected String getFindStatement() {
      return null;
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT INTO FoodPartyMenus  (menuId, newPrice, FoodCount, available) " +
                "VALUES(?, ?, ?, ?)";
    }

    @Override
    protected FoodPartyDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<FoodPartyDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, FoodPartyDAO menuParty) throws SQLException {
        st.setInt(1,menuParty.getMenuId());
        st.setDouble(2,menuParty.getNewPrice());
        st.setInt(3,menuParty.getCount());
        st.setBoolean(4,true);
    }


    public void makeAvailableFoodPartyWithId(int menuId , int count, Double new_price){
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE FoodPartyMenus" +
                    " SET available=?, FoodCount=?, newPrice=?  WHERE menuId=?");
            preparedStatement.setBoolean(1,true);
            preparedStatement.setInt(2,count);
            preparedStatement.setDouble(3,new_price);
            preparedStatement.setInt(4, menuId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();
        }catch (SQLException ignored){
        }
    }

    public boolean insert(FoodPartyDAO menuParty) throws SQLException {
        boolean result = true;
        this.makeAvailableFoodPartyWithId(menuParty.getMenuId() , menuParty.getCount() , menuParty.getNewPrice());
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(getInsertStatement());
        fillInsertValues(preparedStatement, menuParty);
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


    public void DecreaseCountFoodParty(int menuId , int dec_value)throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement("UPDATE FoodPartyMenus" +
                " SET FoodCount=FoodCount - ? WHERE menuId=? AND available=?");
        preparedStatement.setInt(1, dec_value);
        preparedStatement.setInt(2, menuId);
        preparedStatement.setBoolean(3, true);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();
    }



    public void makeUnavailableIfCountFoodPartyZero(int menuId)throws SQLException{
        Connection con = ConnectionPool.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement("UPDATE FoodPartyMenus" +
                " SET available=? WHERE menuId=? AND FoodCount=?");
        preparedStatement.setBoolean(1,false);
        preparedStatement.setInt(2, menuId);
        preparedStatement.setInt(3,0);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();
    }






}
