package guru.springframework.reactive_mongo.repositories;

import guru.springframework.reactive_mongo.domain.Beer;
import guru.springframework.reactive_mongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository <Customer, String> {


    Flux<Customer> findByEmail(String email);
}
