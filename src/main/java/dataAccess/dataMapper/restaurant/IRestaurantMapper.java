package dataAccess.dataMapper.restaurant;

import dataAccess.dataMapper.IMapper;
import domain.entity.Restaurant;
import java.sql.SQLException;


public interface IRestaurantMapper extends IMapper<Restaurant, String> {
	boolean insert(Restaurant restaurant) throws SQLException;
}
