package pl.zajavka.business.dao;

import pl.zajavka.domain.Salesman;

import java.util.Optional;

public interface SalesmanDAO {

    Optional<Salesman> findByPesel(String pesel);
}
