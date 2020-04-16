package dataAccess.dataMapper.user;

import dataAccess.dataMapper.IMapper;
import domain.entity.User;

import java.sql.SQLException;


public interface IUserMapper extends IMapper<User, String> {
	boolean insert(User user) throws SQLException;
}
