package dataAccess.dataMapper.currOrders;

import dataAccess.dataMapper.IMapper;
import domain.entity.Order;

import java.sql.SQLException;


public interface ICurrOrdersMapper extends IMapper<Order, String> {
	boolean insert(Order order) throws SQLException;
}
