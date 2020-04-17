package dataAccess.dataMapper.user;

import dataAccess.dataMapper.IMapper;
import domain.databaseEntity.UserDAO;

import java.sql.SQLException;


public interface IUserMapper extends IMapper<UserDAO, String> {
	boolean insert(UserDAO user) throws SQLException;
}
