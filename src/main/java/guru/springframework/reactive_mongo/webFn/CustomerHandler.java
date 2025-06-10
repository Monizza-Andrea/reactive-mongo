package guru.springframework.reactive_mongo.webFn;


import com.mongodb.internal.connection.Server;
import guru.springframework.reactive_mongo.model.CustomerDTO;
import guru.springframework.reactive_mongo.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {

    private final CustomerService customerService;
    private final Validator validator;


    private void validate(CustomerDTO customerDTO) {

        Errors errors = new BeanPropertyBindingResult(customerDTO, "customerDTO");
        validator.validate(customerDTO, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }

    }


    public Mono<ServerResponse> listCustomer (ServerRequest request) {

        Flux<CustomerDTO> flux;


        if (request.queryParam("email").isPresent()) {
            flux = customerService.listCustomersByEmail(request.queryParam("email").get());
        } else {
            flux = customerService.listCustomers();
        }

        return ServerResponse.ok()
                .body(flux, CustomerDTO.class);

    }

    public Mono<ServerResponse> getCustomerById (ServerRequest request) {

        return ServerResponse.ok()
                .body(customerService.getCustomerById(request.pathVariable("customerId"))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                , CustomerDTO.class);
    }


    public Mono<ServerResponse> createNewCustomer (ServerRequest request) {

        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerService::saveNewCustomer)
                .flatMap(savedCustomer -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(CustomerRouterConfig.CUSTOMER_PATH)
                                .build(savedCustomer.getId()))
                        .build());
    }

    public Mono<ServerResponse> updateCustomerById (ServerRequest request) {

        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerDTO -> customerService.updateCustomer(request.pathVariable("customerId"),customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(updatedCustomer ->
                        ServerResponse.noContent()
                                .build());
    }



    public Mono<ServerResponse> patchCustomerById (ServerRequest request) {

        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerDTO -> customerService.patchCustomer(request.pathVariable("customerId"),customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(updatedCustomer ->
                        ServerResponse.noContent()
                                .build());
    }

    public Mono<ServerResponse> deleteCustomerById (ServerRequest request) {

        return customerService.getCustomerById(request.pathVariable("customerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(customerDTO -> customerService.deleteCustomer(customerDTO.getId()))
                .then(ServerResponse
                        .noContent()
                        .build());
    }


}
