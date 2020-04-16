package dataAccess.dataMapper.orderMenu;

import dataAccess.dataMapper.IMapper;
import domain.entity.Order;

import java.sql.SQLException;


public interface IOrderMenuMapper extends IMapper<Order, String> {
	boolean insert(Order order) throws SQLException;
}
