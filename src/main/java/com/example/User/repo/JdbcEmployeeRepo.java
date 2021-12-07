package com.example.User.repo;


import com.example.User.data.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JdbcEmployeeRepo implements EmployeeRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Employee employee) {

        return jdbcTemplate.update("INSERT INTO employees(name, salary, trainee) VALUES(?,?,?)",
                   new Object[]{ employee.getName(), employee.getSalary(), employee.isTrainee() });
    }

    @Override
    public Employee getMaxId() {
        List<Employee> list = this.getAll();
        return list.get(list.size()-1);
    }

    @Override
    public int update(Employee employee, Long id) {
        return jdbcTemplate.update("UPDATE employees SET name=?, salary=?, trainee=? WHERE id=?",
                new Object[]{employee.getName(), employee.getSalary(), employee.isTrainee(), id });

    }

    @Override
    public Employee findById(Long id) {
        try{
            Employee employee = jdbcTemplate.queryForObject("SELECT * FROM employees WHERE id=?",
                    BeanPropertyRowMapper.newInstance(Employee.class), id);
            return employee;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM employees WHERE id=?", id);

    }

    @Override
    public List<Employee> getAll() {
        return jdbcTemplate.query("SELECT * FROM employees", BeanPropertyRowMapper.newInstance(Employee.class));
    }

    @Override
    public List<Employee> findByTrainee(Boolean trainee) {
        return jdbcTemplate.query("SELECT * FROM employees WHERE trainee= ?", BeanPropertyRowMapper.newInstance(Employee.class), trainee);
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE FROM employees");

    }
}
