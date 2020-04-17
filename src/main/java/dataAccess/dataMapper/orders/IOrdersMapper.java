package dataAccess.dataMapper.orders;

import dataAccess.dataMapper.IMapper;
import domain.databaseEntity.OrdersDAO;
import java.sql.SQLException;


public interface IOrdersMapper extends IMapper<OrdersDAO, String> {
	boolean insert(OrdersDAO order) throws SQLException;
}
