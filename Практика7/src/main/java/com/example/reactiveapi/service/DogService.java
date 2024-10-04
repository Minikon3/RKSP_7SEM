package com.example.reactiveapi.service;

import com.example.reactiveapi.model.Dog;
import com.example.reactiveapi.repository.DogRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DogService {

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Flux<Dog> findAll() {
        return dogRepository.findAll()
                .map(dog -> {
                    // Пример преобразования: делаем имя собаки заглавными буквами
                    dog.setName(dog.getName().toUpperCase());
                    return dog;
                })
                .onErrorResume(e -> {
                    // Обработка ошибок
                    System.err.println("Error fetching all dogs: " + e.getMessage());
                    return Flux.empty(); // Возвращаем пустой Flux при ошибке
                });
    }

    public Mono<Dog> findById(Long id) {
        return dogRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Dog not found with ID: " + id))) // Генерация ошибки, если собака не найдена
                .onErrorResume(e -> {
                    // Обработка ошибок
                    System.err.println("Error fetching dog by ID: " + e.getMessage());
                    return Mono.empty(); // Возвращаем пустой Mono при ошибке
                });
    }

    public Mono<Dog> save(Dog dog) {
        return dogRepository.save(dog)
                .onErrorResume(e -> {
                    // Обработка ошибок
                    System.err.println("Error saving dog: " + e.getMessage());
                    return Mono.empty(); // Возвращаем пустой Mono при ошибке
                });
    }

    public Mono<Dog> update(Dog dog) {
        return dogRepository.findById(dog.getId())
                .flatMap(existingDog -> {
                    existingDog.setName(dog.getName());
                    existingDog.setBreed(dog.getBreed());
                    existingDog.setAge(dog.getAge());
                    return dogRepository.save(existingDog);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Dog not found with ID: " + dog.getId()))) // Генерация ошибки, если собака не найдена
                .onErrorResume(e -> {
                    // Обработка ошибок
                    System.err.println("Error updating dog: " + e.getMessage());
                    return Mono.empty(); // Возвращаем пустой Mono при ошибке
                });
    }

    public Mono<Void> deleteById(Long id) {
        return dogRepository.deleteById(id)
                .onErrorResume(e -> {
                    // Обработка ошибок
                    System.err.println("Error deleting dog with ID " + id + ": " + e.getMessage());
                    return Mono.empty(); // Возвращаем пустой Mono при ошибке
                });
    }
}
