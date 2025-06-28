package guru.springframework.reactive_mongo.services;

import guru.springframework.reactive_mongo.domain.Customer;
import guru.springframework.reactive_mongo.model.BeerDTO;
import guru.springframework.reactive_mongo.model.CustomerDTO;
import guru.springframework.reactive_mongo.webFn.BeerRouterConfig;
import guru.springframework.reactive_mongo.webFn.CustomerRouterConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerEndpointTest {

    @Container
    @ServiceConnection
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(2)
    void testListCustomer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }


    @Test
    @Order(1)
    void testGetCustomerByIdFound() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);

    }

    @Test
    void testGetCustomerByIdNotFound() {

        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();

    }


    @Test
    void testCreateNewCustomer() {
        CustomerDTO customerDTO = getTestCustomerDto();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testCreateNewCustomerBadData() {
        CustomerDTO customerDTO = getTestCustomerDto();
        customerDTO.setEmail("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateCustomer() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateCustomerNotFound() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateCustomerBadData() {
        final String EMAIL = "";
        CustomerDTO customerDTO = getSavedTestCustomer();
        customerDTO.setEmail(EMAIL);

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }



    @Test
    void testPatchCustomer() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }



    @Test
    void testPatchCustomerNotFound() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }



    @Test
    void testDeleteCustomer() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus().isNoContent();
    }



    @Test
    void testDeleteCustomerNotFound() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }



    @Test
    void testPatchCustomerBadData() {
        final String EMAIL = "";
        CustomerDTO customerDTO = getSavedTestCustomer();
        customerDTO.setEmail(EMAIL);

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testListCustomerByEmail() {
        final String EMAIL = "TEST@test";
        CustomerDTO savedCustomer = getSavedTestCustomer();
        savedCustomer.setEmail(EMAIL);

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(savedCustomer), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange();


        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(UriComponentsBuilder
                        .fromPath(CustomerRouterConfig.CUSTOMER_PATH)
                        .queryParam("email", EMAIL).build().toUri())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").value(equalTo(1));
    }

    public CustomerDTO getSavedTestCustomer(){
        FluxExchangeResult<CustomerDTO> customerDTOFluxExchangeResult = webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(CustomerDTO.class);

        List<String> location = customerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange().returnResult(CustomerDTO.class).getResponseBody().blockFirst();
    }


    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("Federico")
                .email("fede@gmail.com")
                .build();
    }

    public static CustomerDTO getTestCustomerDto() {
        return CustomerDTO.builder()
                .customerName("Federico")
                .email("fede@gmail.com")
                .build();
    }
}
