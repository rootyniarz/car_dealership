package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.CustomerDAO;
import pl.zajavka.domain.Customer;
import pl.zajavka.infrastructure.database.entity.CarServiceRequestEntity;
import pl.zajavka.infrastructure.database.entity.CustomerEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CarServiceRequestJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.CustomerJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.InvoiceJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CarServiceRequestEntityMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CustomerEntityMapper;
import pl.zajavka.infrastructure.database.repository.mapper.InvoiceEntityMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository
@AllArgsConstructor
public class CustomerRepository implements CustomerDAO {

    private final CustomerJpaRepository customerJpaRepository;
    private final InvoiceJpaRepository invoiceJpaRepository;
    private final CarServiceRequestJpaRepository carServiceRequestJpaRepository;

    private final CustomerEntityMapper customerEntityMapper;
    private final InvoiceEntityMapper invoiceEntityMapper;
    private final CarServiceRequestEntityMapper carServiceRequestEntityMapper;


    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerJpaRepository.findByEmail(email)
            .map(customerEntityMapper::mapFromEntity);
    }

    @Override
    public void issueInvoice(Customer customer) {
        CustomerEntity customerToSave = customerEntityMapper.mapToEntity(customer);
        CustomerEntity customerSaved = customerJpaRepository.saveAndFlush(customerToSave);

        customer.getInvoices().stream()
            .filter(invoice -> Objects.isNull(invoice.getInvoiceId()))
            .map(invoiceEntityMapper::mapToEntity)
            .forEach(invoiceEntity -> {
                invoiceEntity.setCustomer(customerSaved);
                invoiceJpaRepository.saveAndFlush(invoiceEntity);
            });
    }

    @Override
    public void saveServiceRequest(Customer customer) {
        List<CarServiceRequestEntity> serviceRequests = customer.getCarServiceRequests().stream()
            .filter(serviceRequest -> Objects.isNull(serviceRequest.getCarServiceRequestId()))
            .map(carServiceRequestEntityMapper::mapToEntity)
            .toList();

        serviceRequests
            .forEach(request -> request.setCustomer(customerEntityMapper.mapToEntity(customer)));
        carServiceRequestJpaRepository.saveAllAndFlush(serviceRequests);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        CustomerEntity toSave = customerEntityMapper.mapToEntity(customer);
        CustomerEntity saved = customerJpaRepository.save(toSave);
        return customerEntityMapper.mapFromEntity(saved);
    }
}
