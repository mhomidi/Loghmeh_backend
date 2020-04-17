package dataAccess.dataMapper.orderMenu;

import dataAccess.dataMapper.IMapper;
import domain.databaseEntity.OrderMenuDAO;
import domain.entity.Order;

import java.sql.SQLException;


public interface IOrderMenuMapper extends IMapper<OrderMenuDAO, String> {
	boolean insert(OrderMenuDAO orderMenuDAO) throws SQLException;
}
