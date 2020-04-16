package dataAccess.dataMapper.menu;

import dataAccess.dataMapper.IMapper;
import domain.entity.Menu;

import java.sql.SQLException;


public interface IMenuMapper extends IMapper<Menu, String> {
	boolean insert(Menu menu) throws SQLException;
}
