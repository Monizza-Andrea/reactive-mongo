package guru.springframework.reactive_mongo.services;

import guru.springframework.reactive_mongo.model.BeerDTO;
import reactor.core.publisher.Mono;

public interface BeerService {

    Mono<BeerDTO> saveBeer (BeerDTO beerDTO);

    Mono<BeerDTO> getBeerById(String beerId);
}
