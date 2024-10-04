package com.example.reactiveapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor  // Генерирует конструктор без параметров
@AllArgsConstructor // Генерирует конструктор с параметрами
@Table("dogs")
public class Dog {
    @Id
    private Long id;
    private String name;
    private String breed;
    private Integer age;
}
