package dataAccess.dataMapper.user;

import dataAccess.dataMapper.IMapper;
import domain.databaseEntity.UserDAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


public interface IUserMapper extends IMapper<UserDAO, String> {
	boolean insert(UserDAO user) throws SQLException;
	String getValidateUserStatement();
	boolean validateUser(String id, String password) throws SQLException;
	void fillValidateUserStatement(PreparedStatement st, String id, String pass) throws SQLException;
}
