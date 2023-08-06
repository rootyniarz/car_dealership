package pl.zajavka.business;

import lombok.AllArgsConstructor;
import pl.zajavka.business.dao.ServiceRequestProcessingDAO;
import pl.zajavka.business.management.FileDataPreparationService;
import pl.zajavka.business.management.Keys;
import pl.zajavka.domain.CarServiceProcessingInputData;
import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.domain.Mechanic;
import pl.zajavka.domain.Part;
import pl.zajavka.domain.Service;
import pl.zajavka.domain.ServiceMechanic;
import pl.zajavka.domain.ServicePart;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@org.springframework.stereotype.Service
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
        List<CarServiceProcessingInputData> toProcess = fileDataPreparationService.prepareServiceRequestsToProcess();
        toProcess.forEach(this::processRequest);
    }

    private void processRequest(CarServiceProcessingInputData request) {
        Mechanic mechanic = mechanicService.findMechanic(request.getMechanicPesel());
        carService.findCarToService(request.getCarVin()).orElseThrow();
        CarServiceRequest serviceRequest = carServiceRequestService.findAnyActiveServiceRequest(request.getCarVin());

        Service service = serviceCatalogService.findService(request.getServiceCode());

        ServiceMechanic serviceMechanic = buildServiceMechanic(request, mechanic, serviceRequest, service);

        if (Keys.Constants.FINISHED.toString().equals(request.getDone())) {
            serviceRequest = serviceRequest.withCompletedDateTime(OffsetDateTime.of(2029, 3, 2, 10, 9, 12, 0, ZoneOffset.UTC));
        }

        if (Objects.isNull(request.getPartSerialNumber()) || Objects.isNull(request.getPartQuantity())) {
            serviceRequestProcessingDAO.process(serviceRequest, serviceMechanic);
        } else {
            Part part = partCatalogService.findPart(request.getPartSerialNumber());
            ServicePart servicePart = buildServicePart(request, serviceRequest, part);
            serviceRequestProcessingDAO.process(serviceRequest, serviceMechanic, servicePart);
        }
    }

    private ServiceMechanic buildServiceMechanic(
        CarServiceProcessingInputData request,
        Mechanic mechanic,
        CarServiceRequest serviceRequest,
        Service service
    ) {
        return ServiceMechanic.builder()
            .hours(request.getHours())
            .comment(request.getComment())
            .carServiceRequest(serviceRequest)
            .mechanic(mechanic)
            .service(service)
            .build();
    }

    private ServicePart buildServicePart(
        CarServiceProcessingInputData request,
        CarServiceRequest serviceRequest,
        Part part
    ) {
        return ServicePart.builder()
            .quantity(request.getPartQuantity())
            .carServiceRequest(serviceRequest)
            .part(part)
            .build();
    }
}
