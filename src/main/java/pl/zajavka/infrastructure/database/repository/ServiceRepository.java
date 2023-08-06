package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.ServiceDAO;
import pl.zajavka.domain.Service;
import pl.zajavka.infrastructure.database.repository.jpa.ServiceJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.ServiceEntityMapper;

import java.util.Optional;


@Repository
@AllArgsConstructor
public class ServiceRepository implements ServiceDAO {

    private final ServiceJpaRepository serviceJpaRepository;
    private final ServiceEntityMapper mechanicMapper;

    @Override
    public Optional<Service> findByServiceCode(String serviceCode) {
        return serviceJpaRepository.findByServiceCode(serviceCode)
            .map(mechanicMapper::mapFromEntity);
    }
}
