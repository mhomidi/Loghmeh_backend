package dataAccess.dataMapper;


import dataAccess.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Mapper<T,I> implements IMapper<T,I> {

    abstract protected String getFindStatement();
    abstract protected String getFindAllStatement();
    abstract protected String getInsertStatement();
    abstract protected T convertResultSetToDomainModel(ResultSet rs) throws SQLException;
    abstract protected ArrayList<T> convertResultSetToDomainModelList(ResultSet rs) throws SQLException;
    abstract protected void fillInsertValues(PreparedStatement st, T data) throws SQLException;

    public T find(I id) throws SQLException {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(getFindStatement())
        ) {
            stmt.setString(1, id.toString());
            ResultSet resultSet;
            try {
                resultSet = stmt.executeQuery();
                if(!resultSet.next()) {
                    resultSet.close();
                    stmt.close();
                    con.close();
                    return null;
                }
                T founded = convertResultSetToDomainModel(resultSet);
                resultSet.close();
                stmt.close();
                con.close();
                return founded;
            } catch (SQLException ex) {
                System.out.println("error in Mapper.findByID query.");
                throw ex;
            }
        }
    }

    public List<T> findAll() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement());
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet.isClosed()) {
				st.close();
				con.close();
				return new ArrayList<T>();
			}
            List<T> result = convertResultSetToDomainModelList(resultSet);
            st.close();
        	con.close();
            return result;
        } catch (SQLException e) {
            System.out.println("error in Mapper.findAll query.");
            st.close();
            con.close();
            throw e;
        }
    }

    public boolean insert(T data) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, data);
        try {
	        boolean result = st.execute();
	        st.close();
	        con.close();
	        return result;
        } catch (Exception e) {
			st.close();
			con.close();
			e.printStackTrace();
			return false;
		}
    }

}
