package com.example.reactiveapi.service;

import com.example.reactiveapi.model.Dog;
import com.example.reactiveapi.repository.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class DogServiceTest {

    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private DogService dogService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Dog dog = new Dog(1L, "Buddy", "Golden Retriever", 3);
        given(dogRepository.findAll()).willReturn(Flux.just(dog));

        dogService.findAll()
                .subscribe(result -> {
                    assert result.getName().equals("Buddy");
                });

        verify(dogRepository).findAll();
    }

    @Test
    public void testFindById() {
        Dog dog = new Dog(1L, "Buddy", "Golden Retriever", 3);
        given(dogRepository.findById(1L)).willReturn(Mono.just(dog));

        dogService.findById(1L)
                .subscribe(result -> {
                    assert result.getName().equals("Buddy");
                });

        verify(dogRepository).findById(1L);
    }

    @Test
    public void testSave() {
        Dog dog = new Dog(null, "Buddy", "Golden Retriever", 3);
        Dog savedDog = new Dog(1L, "Buddy", "Golden Retriever", 3);
        given(dogRepository.save(any(Dog.class))).willReturn(Mono.just(savedDog));

        dogService.save(dog)
                .subscribe(result -> {
                    assert result.getId() != null;
                    assert result.getName().equals("Buddy");
                });

        verify(dogRepository).save(any(Dog.class));
    }

    @Test
    public void testDeleteById() {
        given(dogRepository.deleteById(1L)).willReturn(Mono.empty());

        dogService.deleteById(1L)
                .subscribe();

        verify(dogRepository).deleteById(1L);
    }
}
