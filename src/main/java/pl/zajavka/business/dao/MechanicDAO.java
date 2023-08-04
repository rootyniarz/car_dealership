package pl.zajavka.business.dao;

import pl.zajavka.infrastructure.database.entity.MechanicEntity;

import java.util.Optional;

public interface MechanicDAO {

    Optional<MechanicEntity> findByPesel(String pesel);
}
