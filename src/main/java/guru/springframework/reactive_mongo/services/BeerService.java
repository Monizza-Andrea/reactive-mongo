package guru.springframework.reactive_mongo.services;

import guru.springframework.reactive_mongo.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Mono<BeerDTO> findFirstByBeerName(String beerName);

    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> saveBeer (BeerDTO beerDTO);

    Mono<BeerDTO> getBeerById(String beerId);

    Mono<BeerDTO> updateBeer(String beerId, BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(String beerId, BeerDTO beerDTO);

    Mono<Void> deleteBeerById(String beerId);

    Flux<BeerDTO> findByBeerStyle(String beerStyle);
}
