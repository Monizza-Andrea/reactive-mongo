package guru.springframework.reactive_mongo.bootstrap;


import guru.springframework.reactive_mongo.domain.Beer;
import guru.springframework.reactive_mongo.domain.Customer;
import guru.springframework.reactive_mongo.repositories.BeerRepository;
import guru.springframework.reactive_mongo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;


    @Override
    public void run(String... args) throws Exception {

        beerRepository.deleteAll()
                        .doOnSuccess(foo -> {
                            loadBeerData();
                            loadCustomerData();
                        })
                                .subscribe();

    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle("PALE_ALE")
                        .upc("12356")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(122)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Crank")
                        .beerStyle("PALE_ALE")
                        .upc("12356222")
                        .price(new BigDecimal("11.99"))
                        .quantityOnHand(392)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Sunshine City")
                        .beerStyle("IPA")
                        .upc("12356")
                        .price(new BigDecimal("13.99"))
                        .quantityOnHand(144)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe(beer ->
                        System.out.println(beer.toString()));

                beerRepository.save(beer2).subscribe(beer ->
                        System.out.println(beer.toString()));

                beerRepository.save(beer3).subscribe(beer ->
                        System.out.println(beer.toString()));

                System.out.println("Loaded beers: " + beerRepository.count().block());
            }
        });

    }


    private void loadCustomerData() {
        customerRepository.count().subscribe(count -> {
            if (count == 0) {
                Customer customer1 = Customer.builder()
                        .customerName("Andrea Monizza")
                        .email("andrea@gmail.com")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Customer customer2 = Customer.builder()
                        .customerName("Fede Ricco")
                        .email("federico@gmail.com")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Customer customer3 = Customer.builder()
                        .customerName("John Thompson")
                        .email("thompson@gmail.com")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                customerRepository.save(customer1)
                        .subscribe(customer -> System.out.println(customer.toString()));

                customerRepository.save(customer2)
                        .subscribe(customer -> System.out.println(customer.toString()));

                customerRepository.save(customer3)
                        .subscribe(customer -> System.out.println(customer.toString()));

                System.out.println("Loaded customers: " + customerRepository.count().block());
            }
        });
    }


}
