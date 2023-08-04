package pl.zajavka.integration;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import pl.zajavka.business.*;
import pl.zajavka.business.dao.*;
import pl.zajavka.business.management.CarDealershipManagementService;
import pl.zajavka.business.management.FileDataPreparationService;
import pl.zajavka.infrastructure.configuration.HibernateUtil;
import pl.zajavka.infrastructure.database.repository.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarDealershipTest {

    private CarDealershipManagementService carDealershipManagementService;
    private CarPurchaseService carPurchaseService;
    private CarServiceRequestService carServiceRequestService;
    private CarServiceProcessingService carServiceProcessingService;
    private CarService carService;

    @BeforeEach
    void beforeEach() {
        CarDealershipManagementRepository carDealershipManagementDAO = new CarDealershipManagementRepository();
        CarDAO carDAO = new CarRepository();
        SalesmanDAO salesmanDAO = new SalesmanRepository();
        CustomerDAO customerDAO = new CustomerRepository();
        MechanicDAO mechanicDAO = new MechanicRepository();
        ServiceDAO serviceDAO = new ServiceRepository();
        PartDAO partDAO = new PartRepository();
        CarServiceRequestDAO carServiceRequestDAO = new CarServiceRequestRepository();
        FileDataPreparationService fileDataPreparationService = new FileDataPreparationService();
        ServiceCatalogService serviceCatalogService = new ServiceCatalogService(serviceDAO);
        PartCatalogService partCatalogService = new PartCatalogService(partDAO);
        CarService carService = new CarService(carDAO);
        ServiceRequestProcessingDAO serviceRequestProcessingDAO = new ServiceRequestProcessingRepository();
        CustomerService customerService = new CustomerService(customerDAO);
        SalesmanService salesmanService = new SalesmanService(salesmanDAO);
        MechanicService mechanicService = new MechanicService(mechanicDAO);
        this.carDealershipManagementService = new CarDealershipManagementService(
            carDealershipManagementDAO,
            fileDataPreparationService
        );
        this.carPurchaseService = new CarPurchaseService(
            fileDataPreparationService,
            customerService,
            carService,
            salesmanService
        );
        this.carServiceRequestService = new CarServiceRequestService(
            fileDataPreparationService,
            carService,
            customerService,
            carServiceRequestDAO
        );
        this.carServiceProcessingService = new CarServiceProcessingService(
            fileDataPreparationService,
            mechanicService,
            carService,
            serviceCatalogService,
            partCatalogService,
            carServiceRequestService,
            serviceRequestProcessingDAO
        );
        this.carService = new CarService(
            carDAO
        );
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.closeSessionFactory();
    }

    @Test
    @Order(1)
    void purge() {
        log.info("### RUNNING ORDER 1");
        carDealershipManagementService.purge();
    }

    @Test
    @Order(2)
    void init() {
        log.info("### RUNNING ORDER 2");
        carDealershipManagementService.init();
    }

    @Test
    @Order(3)
    void purchase() {
        log.info("### RUNNING ORDER 3");
        carPurchaseService.purchase();
    }

    @Test
    @Order(4)
    void makeServiceRequests() {
        log.info("### RUNNING ORDER 4");
        carServiceRequestService.requestService();
    }

    @Test
    @Order(5)
    void processServiceRequests() {
        log.info("### RUNNING ORDER 5");
        carServiceProcessingService.process();
    }

    @Test
    @Order(6)
    void printCarHistory() {
        log.info("### RUNNING ORDER 6");
        carService.printCarHistory("2C3CDYAG2DH731952");
        carService.printCarHistory("1GCEC19X27Z109567");
    }
}
