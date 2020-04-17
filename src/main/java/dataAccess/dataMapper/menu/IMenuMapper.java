package dataAccess.dataMapper.menu;

import dataAccess.dataMapper.IMapper;
import domain.databaseEntity.MenuDAO;


import java.sql.SQLException;


public interface IMenuMapper extends IMapper<MenuDAO, String> {
	boolean insert(MenuDAO menu) throws SQLException;
}
