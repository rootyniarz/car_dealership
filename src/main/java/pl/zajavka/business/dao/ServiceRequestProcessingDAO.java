package pl.zajavka.business.dao;

import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.domain.ServiceMechanic;
import pl.zajavka.domain.ServicePart;

public interface ServiceRequestProcessingDAO {
    void process(CarServiceRequest serviceRequest, ServiceMechanic serviceMechanic);

    void process(CarServiceRequest serviceRequest, ServiceMechanic serviceMechanic, ServicePart servicePart);
}
