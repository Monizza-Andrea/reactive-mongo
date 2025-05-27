package guru.springframework.reactive_mongo.repositories;

import guru.springframework.reactive_mongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
}
