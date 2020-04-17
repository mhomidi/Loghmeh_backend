package dataAccess.dataMapper.foodPartyMenus;

import dataAccess.dataMapper.IMapper;
import domain.databaseEntity.FoodPartyDAO;
import java.sql.SQLException;


public interface IMenuPartyMapper extends IMapper<FoodPartyDAO, String> {
	boolean insert(FoodPartyDAO menuParty) throws SQLException;
}
