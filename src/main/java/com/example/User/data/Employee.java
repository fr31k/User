package com.example.User.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
//    @Id
//    @Generated
    private Long id;
    private String name;
    private int salary;
    private boolean trainee;
//    private Position position;
//    enum Gender {
//        MALE, FEMALE
//    }
//
    public Employee(String name, int salary, Boolean trainee) {
        this.name = name;
        this.salary = salary;
        this.trainee = trainee;
    }

}
