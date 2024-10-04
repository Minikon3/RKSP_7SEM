package com.example.reactiveapi.controller;

import com.example.reactiveapi.model.Dog;
import com.example.reactiveapi.service.DogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/dogs")
public class DogController {

    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Dog> getAllDogs() {
        return dogService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Dog> getDogById(@PathVariable Long id) {
        return dogService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Dog> createDog(@RequestBody Dog dog) {
        return dogService.save(dog);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Dog> updateDog(@PathVariable Long id, @RequestBody Dog dog) {
        dog.setId(id); // Устанавливаем ID, чтобы обновить соответствующую запись
        return dogService.update(dog);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteDog(@PathVariable Long id) {
        return dogService.deleteById(id);
    }
}
