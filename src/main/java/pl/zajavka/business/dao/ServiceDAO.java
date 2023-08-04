package pl.zajavka.business.dao;

import pl.zajavka.infrastructure.database.entity.ServiceEntity;

import java.util.Optional;

public interface ServiceDAO {

    Optional<ServiceEntity> findByServiceCode(String serviceCode);
}
