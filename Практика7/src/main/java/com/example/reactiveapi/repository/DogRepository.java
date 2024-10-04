package com.example.reactiveapi.repository;

import com.example.reactiveapi.model.Dog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends ReactiveCrudRepository<Dog, Long> {
}
