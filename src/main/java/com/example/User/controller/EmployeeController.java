package com.example.User.controller;

import com.example.User.data.Employee;
import com.example.User.repo.JdbcEmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    JdbcEmployeeRepo employeeRepo;

    @Autowired
    KafkaTemplate<String, Employee> kafkaTemplate;

    private final String TOPIC = "kafka_topic";

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepo.getAll();

            if (employees.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id) {
        Employee employee = employeeRepo.findById(id);

        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/employees/trainee/{trainee}")
    public ResponseEntity<List<Employee>> findByTraining(@PathVariable("trainee") Boolean trainee) {
        try {
            List<Employee> employees = employeeRepo.findByTrainee(trainee);

            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/employees")
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
        try {
            employeeRepo.save(new Employee(employee.getName(), employee.getSalary(), employee.isTrainee()));
            kafkaTemplate.send(TOPIC, employeeRepo.getMaxId());
            return new ResponseEntity<>("Employee was created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("hi 3");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/employees/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable("id") long id, @RequestBody Employee employee) {
        Employee _employee = employeeRepo.findById(id);

        if (_employee != null) {
            _employee.setId(id);
            _employee.setName(employee.getName());
            _employee.setSalary(employee.getSalary());
            _employee.setTrainee(employee.isTrainee());

            kafkaTemplate.send(TOPIC, _employee);
            employeeRepo.update(_employee, id);
            return new ResponseEntity<>("Employee was updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot find Employee with id=" + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
        try {
            int result = employeeRepo.deleteById(id);
            if (result == 0) {
                return new ResponseEntity<>("Cannot find Employee with id=" + id, HttpStatus.OK);
            }
            return new ResponseEntity<>("Employee was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete Employee.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/employees")
    public ResponseEntity<String> deleteAllEmployees() {
        try {
            int numRows = employeeRepo.deleteAll();
            return new ResponseEntity<>("Deleted " + numRows + " Employee(s) successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete employees.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
