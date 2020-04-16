package dataAccess.dataMapper.foodPartyMenus;

import dataAccess.dataMapper.IMapper;
import domain.entity.MenuParty;
import java.sql.SQLException;


public interface IMenuPartyMapper extends IMapper<MenuParty, String> {
	boolean insert(MenuParty menuParty) throws SQLException;
}
