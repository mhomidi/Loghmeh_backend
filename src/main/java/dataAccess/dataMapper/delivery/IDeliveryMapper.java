package dataAccess.dataMapper.delivery;

import dataAccess.dataMapper.IMapper;
import domain.entity.Delivery;
import java.sql.SQLException;


public interface IDeliveryMapper extends IMapper<Delivery, String> {
	boolean insert(Delivery user) throws SQLException;
}
