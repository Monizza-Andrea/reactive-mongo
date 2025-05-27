package guru.springframework.reactive_mongo.repositories;

import guru.springframework.reactive_mongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository <Customer, String> {
}
