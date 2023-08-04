package pl.zajavka.business;

import lombok.AllArgsConstructor;
import pl.zajavka.business.dao.ServiceRequestProcessingDAO;
import pl.zajavka.business.management.FileDataPreparationService;
import pl.zajavka.business.management.Keys;
import pl.zajavka.domain.CarServiceProcessingRequest;
import pl.zajavka.infrastructure.database.entity.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class CarServiceProcessingService {

    private final FileDataPreparationService fileDataPreparationService;
    private final MechanicService mechanicService;
    private final CarService carService;
    private final ServiceCatalogService serviceCatalogService;
    private final PartCatalogService partCatalogService;
    private final CarServiceRequestService carServiceRequestService;
    private final ServiceRequestProcessingDAO serviceRequestProcessingDAO;

    public void process() {
        List<CarServiceProcessingRequest> toProcess = fileDataPreparationService.prepareServiceRequestsToProcess();
        toProcess.forEach(this::processRequest);
    }

    private void processRequest(CarServiceProcessingRequest request) {
        MechanicEntity mechanic = mechanicService.findMechanic(request.getMechanicPesel());
        carService.findCarToService(request.getCarVin()).orElseThrow();
        CarServiceRequestEntity serviceRequest = carServiceRequestService.findAnyActiveServiceRequest(request.getCarVin());

        ServiceEntity service = serviceCatalogService.findService(request.getServiceCode());

        ServiceMechanicEntity serviceMechanicEntity = buildServiceMechanicEntity(request, mechanic, serviceRequest, service);

        if (Keys.Constants.FINISHED.toString().equals(request.getDone())) {
            serviceRequest.setCompletedDateTime(OffsetDateTime.of(2029, 3, 2, 10, 9, 12, 0, ZoneOffset.UTC));
        }

        if (Objects.isNull(request.getPartSerialNumber()) || Objects.isNull(request.getPartQuantity())) {
            serviceRequestProcessingDAO.process(serviceRequest, serviceMechanicEntity);
        } else {
            PartEntity part = partCatalogService.findPart(request.getPartSerialNumber());
            ServicePartEntity servicePartEntity = buildServicePartEntity(request, serviceRequest, part);
            serviceRequestProcessingDAO.process(serviceRequest, serviceMechanicEntity, servicePartEntity);
        }
    }

    private ServiceMechanicEntity buildServiceMechanicEntity(
        CarServiceProcessingRequest request,
        MechanicEntity mechanic,
        CarServiceRequestEntity serviceRequest,
        ServiceEntity service
    ) {
        return ServiceMechanicEntity.builder()
            .hours(request.getHours())
            .comment(request.getComment())
            .carServiceRequest(serviceRequest)
            .mechanic(mechanic)
            .service(service)
            .build();
    }

    private ServicePartEntity buildServicePartEntity(
        CarServiceProcessingRequest request,
        CarServiceRequestEntity serviceRequest,
        PartEntity part
    ) {
        return ServicePartEntity.builder()
            .quantity(request.getPartQuantity())
            .carServiceRequest(serviceRequest)
            .part(part)
            .build();
    }
}
