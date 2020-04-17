package dataAccess.dataMapper.user;


import dataAccess.ConnectionPool;
import dataAccess.dataMapper.Mapper;
import domain.databaseEntity.UserDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    protected String getFindAllStatement() {
        return null;
    }



    @Override
    protected String getFindStatement() {
      return null;
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT INTO Users (username, firstname, lastname, password, email, phone, credit) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected UserDAO convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected ArrayList<UserDAO> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        return null;
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


    public void  registerUser(UserDAO user){
        //if not find add user
        try {
            this.insert(user);
        }catch (SQLException e){

        }
    }


    public boolean validateUser(String username,String password){
        return true;
    }







}
