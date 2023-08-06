package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.business.management.FileDataPreparationService;
import pl.zajavka.domain.CarPurchaseRequestInputData;
import pl.zajavka.domain.CarToBuy;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Invoice;
import pl.zajavka.domain.Salesman;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarPurchaseService {

    private final FileDataPreparationService fileDataPreparationService;
    private final CustomerService customerService;
    private final CarService carService;
    private final SalesmanService salesmanService;

    public void purchase() {
        var firstTimeData = fileDataPreparationService.prepareFirstTimePurchaseData();
        var nextTimeData = fileDataPreparationService.prepareNextTimePurchaseData();

        List<Customer> firstTimeCustomers = firstTimeData.stream()
            .map(this::createFirstTimeToBuyCustomer)
            .toList();
        firstTimeCustomers.forEach(customerService::issueInvoice);

        List<Customer> nextTimeCustomers = nextTimeData.stream()
            .map(this::createNextTimeToBuyCustomer)
            .toList();
        nextTimeCustomers.forEach(customerService::issueInvoice);
    }

    private Customer createFirstTimeToBuyCustomer(CarPurchaseRequestInputData inputData) {
        CarToBuy car = carService.findCarToBuy(inputData.getCarVin());
        Salesman salesman = salesmanService.findSalesman(inputData.getSalesmanPesel());
        Invoice invoice = buildInvoice(car, salesman);

        return fileDataPreparationService.buildCustomer(inputData, invoice);
    }

    private Customer createNextTimeToBuyCustomer(CarPurchaseRequestInputData inputData) {
        Customer existingCustomer = customerService.findCustomer(inputData.getCustomerEmail());
        CarToBuy car = carService.findCarToBuy(inputData.getCarVin());
        Salesman salesman = salesmanService.findSalesman(inputData.getSalesmanPesel());
        Invoice invoice = buildInvoice(car, salesman);
        Set<Invoice> existingInvoices = existingCustomer.getInvoices();
        existingInvoices.add(invoice);
        return existingCustomer.withInvoices(existingInvoices);
    }

    private Invoice buildInvoice(CarToBuy car, Salesman salesman) {
        return Invoice.builder()
            .invoiceNumber(UUID.randomUUID().toString())
            .dateTime(OffsetDateTime.of(2025, 10, 1, 12, 0, 0, 0, ZoneOffset.UTC))
            .car(car)
            .salesman(salesman)
            .build();
    }
}
