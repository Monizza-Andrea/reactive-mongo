package guru.springframework.reactive_mongo.services;

import guru.springframework.reactive_mongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<Void> deleteCustomer(String customerId);

    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> getCustomerById(String customerId);

    Flux<CustomerDTO> listCustomers();
}
