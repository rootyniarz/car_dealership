package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.SalesmanDAO;
import pl.zajavka.domain.Salesman;
import pl.zajavka.infrastructure.database.repository.jpa.SalesmanJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.SalesmanEntityMapper;

import java.util.Optional;


@Repository
@AllArgsConstructor
public class SalesmanRepository implements SalesmanDAO {

    private final SalesmanJpaRepository salesmanJpaRepository;
    private final SalesmanEntityMapper salesmanEntityMapper;

    @Override
    public Optional<Salesman> findByPesel(String pesel) {
        return salesmanJpaRepository.findByPesel(pesel)
            .map(salesmanEntityMapper::mapFromEntity);
    }
}
