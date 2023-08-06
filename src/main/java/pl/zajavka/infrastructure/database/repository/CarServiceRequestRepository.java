package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.CarServiceRequestDAO;
import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.infrastructure.database.repository.jpa.CarServiceRequestJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CarServiceRequestEntityMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CarServiceRequestRepository implements CarServiceRequestDAO {

    private final CarServiceRequestJpaRepository carServiceRequestJpaRepository;
    private final CarServiceRequestEntityMapper carServiceRequestEntityMapper;

    @Override
    public Set<CarServiceRequest> findActiveServiceRequestsByCarVin(String carVin) {
        return carServiceRequestJpaRepository.findActiveServiceRequestsByCarVin(carVin).stream()
            .map(carServiceRequestEntityMapper::mapFromEntity)
            .collect(Collectors.toSet());
    }
}
