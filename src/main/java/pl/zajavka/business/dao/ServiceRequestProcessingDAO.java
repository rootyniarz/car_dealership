package pl.zajavka.business.dao;

import pl.zajavka.infrastructure.database.entity.CarServiceRequestEntity;
import pl.zajavka.infrastructure.database.entity.ServiceMechanicEntity;
import pl.zajavka.infrastructure.database.entity.ServicePartEntity;

public interface ServiceRequestProcessingDAO {
    void process(CarServiceRequestEntity serviceRequest, ServiceMechanicEntity serviceMechanicEntity);

    void process(CarServiceRequestEntity serviceRequest, ServiceMechanicEntity serviceMechanicEntity, ServicePartEntity servicePartEntity);
}
