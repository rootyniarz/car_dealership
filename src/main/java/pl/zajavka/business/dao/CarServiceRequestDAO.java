package pl.zajavka.business.dao;

import pl.zajavka.infrastructure.database.entity.CarServiceRequestEntity;

import java.util.Set;

public interface CarServiceRequestDAO {
    Set<CarServiceRequestEntity> findActiveServiceRequestsByCarVin(String carVin);
}
