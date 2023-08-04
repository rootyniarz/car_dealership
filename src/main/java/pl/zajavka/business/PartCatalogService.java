package pl.zajavka.business;

import lombok.AllArgsConstructor;
import pl.zajavka.business.dao.PartDAO;
import pl.zajavka.infrastructure.database.entity.PartEntity;

import java.util.Optional;

@AllArgsConstructor
public class PartCatalogService {

    private final PartDAO partDAO;

    public PartEntity findPart(String partSerialNumber) {
        Optional<PartEntity> part = partDAO.findBySerialNumber(partSerialNumber);
        if (part.isEmpty()) {
            throw new RuntimeException("Could not find part by part serial number: [%s]".formatted(partSerialNumber));
        }
        return part.get();
    }
}
