package dataAccess.dataMapper.orders;

import dataAccess.dataMapper.IMapper;
import domain.entity.Menu;
import domain.entity.Order;

import java.sql.SQLException;


public interface IOrdersMapper extends IMapper<Order, String> {
	boolean insert(Order order) throws SQLException;
}
