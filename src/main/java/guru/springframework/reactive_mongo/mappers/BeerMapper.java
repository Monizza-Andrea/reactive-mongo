package guru.springframework.reactive_mongo.mappers;

import guru.springframework.reactive_mongo.domain.Beer;
import guru.springframework.reactive_mongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDto(Beer beer);
    Beer beerDtoToBeer(BeerDTO beerDTO);
}
