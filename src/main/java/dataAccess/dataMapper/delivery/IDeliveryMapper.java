package dataAccess.dataMapper.delivery;

import dataAccess.dataMapper.IMapper;
import domain.databaseEntity.DeliveryDAO;
import java.sql.SQLException;


public interface IDeliveryMapper extends IMapper<DeliveryDAO, String> {
	boolean insert(DeliveryDAO delivery) throws SQLException;
}
