package com.example.reactiveapi.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class DatabaseInitializer {

    @Bean
    public ApplicationRunner runner(ConnectionFactory connectionFactory) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Mono.from(connectionFactory.create())
                        .flatMapMany(connection ->
                                Mono.from(connection.createStatement(
                                                "CREATE TABLE IF NOT EXISTS dogs (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), breed VARCHAR(255), age INT)")
                                        .execute())
                        )
                        .subscribe();
            }
        };
    }
}
