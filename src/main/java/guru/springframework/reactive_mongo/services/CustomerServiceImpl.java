package guru.springframework.reactive_mongo.services;


import guru.springframework.reactive_mongo.mappers.CustomerMapper;
import guru.springframework.reactive_mongo.model.CustomerDTO;
import guru.springframework.reactive_mongo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<Void> deleteCustomer(String customerId) {
        return customerRepository.deleteById(customerId);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId).map(foundCustomer -> {
                    if (StringUtils.hasText(customerDTO.getCustomerName())) foundCustomer.setCustomerName(customerDTO.getCustomerName());
                    if (StringUtils.hasText(customerDTO.getEmail())) foundCustomer.setEmail(customerDTO.getEmail());
                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundCustomer -> {
//                  updating properties

                    foundCustomer.setCustomerName(customerDTO.getCustomerName());
                    foundCustomer.setEmail(customerDTO.getEmail());

                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO))
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(String customerId) {

        return customerRepository.findById(customerId)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Flux<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Flux<CustomerDTO> listCustomersByEmail (String email) {
        return customerRepository.findByEmail(email)
                .map(customerMapper::customerToCustomerDto);
    }
}

