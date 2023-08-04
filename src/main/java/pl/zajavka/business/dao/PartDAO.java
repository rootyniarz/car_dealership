package pl.zajavka.business.dao;

import pl.zajavka.infrastructure.database.entity.PartEntity;

import java.util.Optional;

public interface PartDAO {

    Optional<PartEntity> findBySerialNumber(String serialNumber);
}
