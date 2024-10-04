package com.example.prac4.controller;

import com.example.prac4.model.Product;
import com.example.prac4.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class RSocketController {

    private final ProductRepository productRepository;

    // Request-Response: Возвращает один продукт по ID
    @MessageMapping("request-response")
    public Mono<Product> requestResponse(Long productId) {
        System.out.println("Received request for product ID: " + productId);
        return productRepository.findById(productId)
                .doOnError(error -> System.err.println("Error during Request-Response: " + error.getMessage()));
    }

    // Request-Stream: Возвращает поток всех продуктов
    @MessageMapping("request-stream")
    public Flux<Product> requestStream() {
        System.out.println("Received request for all products");
        return productRepository.findAll()
                .doOnError(error -> System.err.println("Error during Request-Stream: " + error.getMessage()));
    }

    // Fire-and-Forget: Добавляет новый продукт в базу данных
    @MessageMapping("fire-and-forget")
    public Mono<Void> fireAndForget(Product product) {
        System.out.println("Received product for saving: " + product);
        return productRepository.save(product)
                .doOnError(error -> System.err.println("Error during Fire-and-Forget: " + error.getMessage()))
                .then();
    }

    // Channel: Принимает поток продуктов и возвращает их цены
    @MessageMapping("channel")
    public Flux<Double> channel(Flux<Product> products) {
        System.out.println("Received products for price extraction");
        return products
                .map(Product::getPrice)
                .doOnError(error -> System.err.println("Error during Channel: " + error.getMessage()));
    }
}
