package guru.springframework.reactive_mongo.services;

import guru.springframework.reactive_mongo.domain.Beer;
import guru.springframework.reactive_mongo.mappers.BeerMapper;
import guru.springframework.reactive_mongo.model.BeerDTO;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(getTestBeer());
    }

    @Test
    void saveBeer() throws InterruptedException {

        Mono<BeerDTO> savedMono = beerService.saveBeer(beerDTO);
        savedMono.subscribe(savedDto -> System.out.println(savedDto.getId()));

        Thread.sleep(1000l);
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(22)
                .upc("123213")
                .build();
    }


}