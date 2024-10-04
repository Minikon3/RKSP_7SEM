package com.example.prac4_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Prac4ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(Prac4ClientApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RSocketRequester.Builder builder) {
		return args -> {
			RSocketRequester requester = builder.connectTcp("localhost", 7000).block();

			// Пример Request-Response
			requester
					.route("request-response")
					.data(1L) // ID продукта
					.retrieveMono(Product.class)
					.doOnError(error -> System.err.println("Error during Request-Response: " + error.getMessage()))
					.subscribe(product -> System.out.println("Received product: " + product));

			// Пример Request-Stream
			requester
					.route("request-stream")
					.retrieveFlux(Product.class)
					.doOnError(error -> System.err.println("Error during Request-Stream: " + error.getMessage()))
					.subscribe(product -> System.out.printf("Received product: %s%n", product));

			// Пример Fire-and-Forget
			requester
					.route("fire-and-forget")
					.data(new Product(null, "New Product", 99.99))
					.send()
					.doOnError(error -> System.err.println("Error during Fire-and-Forget: " + error.getMessage()))
					.subscribe();

			// Пример Channel
			requester
					.route("channel")
					.data(Flux.just(
							new Product(1L, "Product1", 10.0),
							new Product(2L, "Product2", 20.0)
					))
					.retrieveFlux(Double.class)
					.doOnError(error -> System.err.println("Error during Channel: " + error.getMessage()))
					.subscribe(price -> System.out.printf("Received price: %.2f%n", price));
		};
	}

	// Модель данных
	public static class Product {
		private Long id;
		private String name;
		private Double price;

		// Конструкторы, геттеры и сеттеры
		public Product() {}

		public Product(Long id, String name, Double price) {
			this.id = id;
			this.name = name;
			this.price = price;
		}

		public Long getId() { return id; }
		public void setId(Long id) { this.id = id; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public Double getPrice() { return price; }
		public void setPrice(Double price) { this.price = price; }

		@Override
		public String toString() {
			return String.format("Product{id=%d, name='%s', price=%.2f}", id, name, price);
		}
	}
}
