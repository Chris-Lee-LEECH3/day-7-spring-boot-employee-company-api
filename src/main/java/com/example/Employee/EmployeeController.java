package com.example.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;

    public void clear() {
        employees.clear();
        id = 0;
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
    public List<Employee> getAllEmployees(
        @RequestParam(required = false) String gender,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, employees.size());
            if (start >= 0 && start < employees.size()) {
                return employees.subList(start, end);
            }
        }

        if (gender == null) {
            return employees;
        }

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

    @PutMapping("/{id}")
    public Employee update(@PathVariable Integer id, @RequestBody Employee employee) {
        Employee existingEmployee = this.findEmployeeById(id);
        if (existingEmployee != null) {
            Employee updatedEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
            employees.remove(existingEmployee);
            employees.add(updatedEmployee);
            return updatedEmployee;
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Employee existingEmployee = this.findEmployeeById(id);
        if (existingEmployee != null) {
            employees.remove(existingEmployee);
        }
    }

}
