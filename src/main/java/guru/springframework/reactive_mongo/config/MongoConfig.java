package guru.springframework.reactive_mongo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMongoAuditing
public class MongoConfig {


    @Bean
    public ReactiveAuditorAware<String> auditorAware() {
        return () -> Mono.just("system");
    }
}
