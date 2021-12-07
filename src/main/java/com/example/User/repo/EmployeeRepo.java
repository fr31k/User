package com.example.User.repo;



import com.example.User.data.Employee;

import java.util.List;

public interface EmployeeRepo {


    int save(Employee employee);

    int update(Employee employee, Long id);

    Employee findById(Long id);

    int deleteById(Long id);

    Employee getMaxId();

    List<Employee> getAll();

    List<Employee> findByTrainee(Boolean trainee);

    int deleteAll();
}
