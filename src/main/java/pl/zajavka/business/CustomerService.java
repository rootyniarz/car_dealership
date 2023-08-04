package pl.zajavka.business;

import lombok.AllArgsConstructor;
import pl.zajavka.business.dao.CustomerDAO;
import pl.zajavka.domain.CarServiceRequest;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.CustomerEntity;

import java.util.Optional;

@AllArgsConstructor
public class CustomerService {

    private final CustomerDAO customerDAO;

    public void issueInvoice(CustomerEntity customer) {
        customerDAO.issueInvoice(customer);
    }

    public CustomerEntity findCustomer(String email) {
        Optional<CustomerEntity> customer = customerDAO.findByEmail(email);
        if (customer.isEmpty()) {
            throw new RuntimeException("Could not find customer by email: [%s]".formatted(email));
        }
        return customer.get();
    }

    public void saveServiceRequest(CustomerEntity customer) {
        customerDAO.saveServiceRequest(customer);
    }

    public CustomerEntity saveCustomer(CarServiceRequest.Customer customer) {
        CustomerEntity entity = CustomerEntity.builder()
            .name(customer.getName())
            .surname(customer.getSurname())
            .phone(customer.getPhone())
            .email(customer.getEmail())
            .address(AddressEntity.builder()
                .country(customer.getAddress().getCountry())
                .city(customer.getAddress().getCity())
                .postalCode(customer.getAddress().getPostalCode())
                .address(customer.getAddress().getAddress())
                .build())
            .build();
        return customerDAO.saveCustomer(entity);
    }
}
