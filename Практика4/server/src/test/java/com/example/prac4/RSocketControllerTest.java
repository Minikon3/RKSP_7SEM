package com.example.prac4;

import com.example.prac4.controller.RSocketController;
import com.example.prac4.model.Product;
import com.example.prac4.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class RSocketControllerTest {

	private ProductRepository productRepository;
	private RSocketController rSocketController;

	@BeforeEach
	public void setUp() {
		productRepository = mock(ProductRepository.class);
		rSocketController = new RSocketController(productRepository);
	}

	@Test
	public void testRequestResponse() {
		Product product = new Product(1L, "Test Product", 100.0);
		when(productRepository.findById(1L)).thenReturn(Mono.just(product));

		Mono<Product> response = rSocketController.requestResponse(1L);

		StepVerifier.create(response)
				.expectNext(product)
				.verifyComplete();

		verify(productRepository, times(1)).findById(1L);
	}

	@Test
	public void testRequestStream() {
		Product product1 = new Product(1L, "Product 1", 10.0);
		Product product2 = new Product(2L, "Product 2", 20.0);
		when(productRepository.findAll()).thenReturn(Flux.just(product1, product2));

		Flux<Product> response = rSocketController.requestStream();

		StepVerifier.create(response)
				.expectNext(product1)
				.expectNext(product2)
				.verifyComplete();

		verify(productRepository, times(1)).findAll();
	}

	@Test
	public void testFireAndForget() {
		Product product = new Product(null, "New Product", 99.99);
		when(productRepository.save(product)).thenReturn(Mono.just(new Product(1L, "New Product", 99.99)));

		Mono<Void> response = rSocketController.fireAndForget(product);

		StepVerifier.create(response)
				.verifyComplete();

		verify(productRepository, times(1)).save(product);
	}

	@Test
	public void testChannel() {
		Product product1 = new Product(1L, "Product 1", 10.0);
		Product product2 = new Product(2L, "Product 2", 20.0);
		Flux<Product> products = Flux.just(product1, product2);

		Flux<Double> response = rSocketController.channel(products);

		StepVerifier.create(response)
				.expectNext(10.0)
				.expectNext(20.0)
				.verifyComplete();
	}
}
