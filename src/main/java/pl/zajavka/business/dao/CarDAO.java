package pl.zajavka.business.dao;

import pl.zajavka.infrastructure.database.entity.CarHistoryEntity;
import pl.zajavka.infrastructure.database.entity.CarToBuyEntity;
import pl.zajavka.infrastructure.database.entity.CarToServiceEntity;

import java.util.Optional;

public interface CarDAO {

    Optional<CarToBuyEntity> findCarToBuyByVin(String vin);

    Optional<CarToServiceEntity> findCarToServiceByVin(String vin);

    CarToServiceEntity saveCarToService(CarToServiceEntity entity);

    CarHistoryEntity findCarHistoryByVin(String vin);
}
