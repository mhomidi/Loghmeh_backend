package dataAccess.dataMapper.restaurant;

import dataAccess.dataMapper.IMapper;
import domain.databaseEntity.RestaurantDAO;
import java.sql.SQLException;


public interface IRestaurantMapper extends IMapper<RestaurantDAO, String> {
	boolean insert(RestaurantDAO restaurant) throws SQLException;
}
