package pl.zajavka.business.dao.management;

import java.util.List;

public interface CarDealershipManagementDAO {

    void purge();

    void saveAll(List<?> entities);
}
