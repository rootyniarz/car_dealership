package pl.zajavka.business.dao;

import pl.zajavka.infrastructure.database.entity.SalesmanEntity;

import java.util.Optional;

public interface SalesmanDAO {

    Optional<SalesmanEntity> findByPesel(String pesel);
}
