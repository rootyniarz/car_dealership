package pl.zajavka.business;

import lombok.AllArgsConstructor;
import pl.zajavka.business.dao.SalesmanDAO;
import pl.zajavka.infrastructure.database.entity.SalesmanEntity;

import java.util.Optional;

@AllArgsConstructor
public class SalesmanService {

    private final SalesmanDAO salesmanDAO;

    public SalesmanEntity findSalesman(String pesel) {
        Optional<SalesmanEntity> salesman = salesmanDAO.findByPesel(pesel);
        if (salesman.isEmpty()) {
            throw new RuntimeException("Could not find salesman by pesel: [%s]".formatted(pesel));
        }
        return salesman.get();
    }
}
