package guru.springframework.reactive_mongo.mappers;

import guru.springframework.reactive_mongo.domain.Beer;
import guru.springframework.reactive_mongo.domain.Customer;
import guru.springframework.reactive_mongo.model.BeerDTO;
import guru.springframework.reactive_mongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDto(Customer customer);
    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
