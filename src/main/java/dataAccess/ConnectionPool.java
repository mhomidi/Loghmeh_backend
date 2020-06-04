package dataAccess;

import org.apache.commons.dbcp.*;
import java.sql.*;


public class ConnectionPool {

    private static BasicDataSource ds = new BasicDataSource();
    private final static String dbURL = "jdbc:mysql://db:3306/loghmeh?useUnicode=yes&characterEncoding=UTF-8";

    static {
    	ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(dbURL);
    	ds.setUsername("root");
    	ds.setPassword("root");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        setEncoding();
    }

    public static String getAlterEncodingString(){
        return "ALTER DATABASE loghmeh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
    }

    public static void setEncoding(){
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute(getAlterEncodingString());
            connection.close();
            statement.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        while (true) {
            try {
                return ds.getConnection();
            } catch (SQLException ignored){
                System.out.println("Retry to connect....!");
            }
        }
    }
}