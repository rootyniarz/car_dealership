package pl.zajavka.business.management;

import pl.zajavka.domain.CarServiceProcessingRequest;
import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.infrastructure.database.entity.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileDataPreparationService {

    public List<?> prepareInitData() {
        List<SalesmanEntity> salesmen = InputDataCache
            .getInputData(Keys.InputDataGroup.INIT, Keys.Entity.SALESMAN, InputDataMapper::mapSalesman);
        List<MechanicEntity> mechanics = InputDataCache
            .getInputData(Keys.InputDataGroup.INIT, Keys.Entity.MECHANIC, InputDataMapper::mapMechanic);
        List<CarToBuyEntity> cars = InputDataCache
            .getInputData(Keys.InputDataGroup.INIT, Keys.Entity.CAR, InputDataMapper::mapCarToBuy);
        List<ServiceEntity> services = InputDataCache
            .getInputData(Keys.InputDataGroup.INIT, Keys.Entity.SERVICE, InputDataMapper::mapService);
        List<PartEntity> parts = InputDataCache
            .getInputData(Keys.InputDataGroup.INIT, Keys.Entity.PART, InputDataMapper::mapPart);

        return Stream.of(salesmen, mechanics, cars, services, parts)
            .flatMap(Collection::stream)
            .toList();
    }

    public List<Map<String, List<String>>> prepareFirstTimePurchaseData() {
        return InputDataCache.getInputData(Keys.InputDataGroup.BUY_FIRST_TIME, this::prepareMap);
    }

    public List<Map<String, List<String>>> prepareNextTimePurchaseData() {
        return InputDataCache.getInputData(Keys.InputDataGroup.BUY_AGAIN, this::prepareMap);
    }

    public CustomerEntity buildCustomerEntity(List<String> inputData, InvoiceEntity invoice) {
        return CustomerEntity.builder()
            .name(inputData.get(0))
            .surname(inputData.get(1))
            .phone(inputData.get(2))
            .email(inputData.get(3))
            .address(AddressEntity.builder()
                .country(inputData.get(4))
                .city(inputData.get(5))
                .postalCode(inputData.get(6))
                .address(inputData.get(7))
                .build())
            .invoices(Set.of(invoice))
            .build();
    }

    public List<CarServiceRequest> createCarServiceRequests() {
        return InputDataCache.getInputData(Keys.InputDataGroup.SERVICE_REQUEST, this::prepareMap).stream()
            .map(this::createCarServiceRequest)
            .toList();
    }

    private CarServiceRequest createCarServiceRequest(Map<String, List<String>> inputData) {
        return CarServiceRequest.builder()
            .customer(createCustomer(inputData.get(Keys.Entity.CUSTOMER.toString())))
            .car(createCar(inputData.get(Keys.Entity.CAR.toString())))
            .customerComment(inputData.get(Keys.Constants.WHAT.toString()).get(0))
            .build();
    }

    private CarServiceRequest.Customer createCustomer(List<String> inputData) {
        if (inputData.size() == 1) {
            return CarServiceRequest.Customer.builder()
                .email(inputData.get(0))
                .build();
        }
        return CarServiceRequest.Customer.builder()
            .name(inputData.get(0))
            .surname(inputData.get(1))
            .phone(inputData.get(2))
            .email(inputData.get(3))
            .address(CarServiceRequest.Address.builder()
                    .country(inputData.get(4))
                    .city(inputData.get(5))
                    .postalCode(inputData.get(6))
                .address(inputData.get(7))
                .build())
            .build();
    }

    private CarServiceRequest.Car createCar(List<String> inputData) {
        if (inputData.size() == 1) {
            return CarServiceRequest.Car.builder()
                .vin(inputData.get(0))
                .build();
        }
        return CarServiceRequest.Car.builder()
            .vin(inputData.get(0))
            .brand(inputData.get(1))
            .model(inputData.get(2))
            .year(Integer.parseInt(inputData.get(3)))
            .build();
    }

    public List<CarServiceProcessingRequest> prepareServiceRequestsToProcess() {
        return InputDataCache.getInputData(Keys.InputDataGroup.DO_THE_SERVICE, this::prepareMap).stream()
            .map(this::createCarServiceRequestToProcess)
            .toList();
    }

    private CarServiceProcessingRequest createCarServiceRequestToProcess(Map<String, List<String>> inputData) {
        List<String> whats = inputData.get(Keys.Constants.WHAT.toString());
        return CarServiceProcessingRequest.builder()
            .mechanicPesel(inputData.get(Keys.Entity.MECHANIC.toString()).get(0))
            .carVin(inputData.get(Keys.Entity.CAR.toString()).get(0))
            .partSerialNumber(Optional.ofNullable(whats.get(0)).filter(value -> !value.isBlank()).orElse(null))
            .partQuantity(Optional.ofNullable(whats.get(1)).filter(value -> !value.isBlank()).map(Integer::parseInt).orElse(null))
            .serviceCode(whats.get(2))
            .hours(Integer.parseInt(whats.get(3)))
            .comment(whats.get(4))
            .done(whats.get(5))
            .build();
    }

    private Map<String, List<String>> prepareMap(String line) {
        List<String> grouped = Arrays.stream(line.split("->")).map(String::trim).toList();
        return IntStream.iterate(0, previous -> previous + 2)
            .boxed()
            .limit(3)
            .collect(Collectors.toMap(grouped::get, i -> List.of(grouped.get(i + 1).split(";"))));
    }
}
