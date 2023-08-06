package pl.zajavka.business.dao;

import pl.zajavka.domain.Part;

import java.util.Optional;

public interface PartDAO {

    Optional<Part> findBySerialNumber(String serialNumber);
}
