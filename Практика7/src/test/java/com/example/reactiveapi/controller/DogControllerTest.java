package com.example.reactiveapi.controller;

import com.example.reactiveapi.model.Dog;
import com.example.reactiveapi.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(DogController.class)
public class DogControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DogService dogService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(dogService);
    }

    @Test
    public void testGetAllDogs() {
        given(dogService.findAll()).willReturn(Flux.just(new Dog(1L, "Buddy", "Golden Retriever", 3)));

        webTestClient.get().uri("/api/dogs")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Dog.class)
                .hasSize(1);
    }

    @Test
    public void testGetDogById() {
        given(dogService.findById(1L)).willReturn(Mono.just(new Dog(1L, "Buddy", "Golden Retriever", 3)));

        webTestClient.get().uri("/api/dogs/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Dog.class)
                .value(dog -> {
                    assert dog.getName().equals("Buddy");
                });
    }

    @Test
    public void testCreateDog() {
        Dog dog = new Dog(null, "Buddy", "Golden Retriever", 3);
        Dog savedDog = new Dog(1L, "Buddy", "Golden Retriever", 3);

        given(dogService.save(any(Dog.class))).willReturn(Mono.just(savedDog));

        webTestClient.post().uri("/api/dogs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dog)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Dog.class)
                .value(d -> {
                    // Используйте Assertions для проверки значений
                    assert d.getId() != null;
                    assert "Buddy".equals(d.getName());
                    assert "Golden Retriever".equals(d.getBreed());
                    assert d.getAge() == 3;
                });
    }

    @Test
    public void testDeleteDog() {
        given(dogService.deleteById(1L)).willReturn(Mono.empty());

        webTestClient.delete().uri("/api/dogs/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
