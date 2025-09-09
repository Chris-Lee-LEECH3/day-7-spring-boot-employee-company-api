package com.example.Employee;

import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;

    public void clear() {
        employees.clear();
    }

    @GetMapping("/{id}")
    public Employee findEmployeeById(@PathVariable Integer id) {
        for (Employee employee : employees) {
            if (employee.id().equals(id)) {
                return employee;
            }
        }
        return null;
    }

    @GetMapping
    public List<Employee> getAllEmployees(@RequestParam(required = false) String gender) {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.gender().equalsIgnoreCase(gender)) {
                result.add(employee);
            }
        }
        return result;
//        return employees.stream().filter(employee -> employee.gender().equalsIgnoreCase(gender)).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee newEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }

}
