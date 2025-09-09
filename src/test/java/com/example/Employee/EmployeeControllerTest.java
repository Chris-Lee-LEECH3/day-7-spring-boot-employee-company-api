package com.example.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        employeeController.clear();
    }

    @Test
    public void should_return_created_employee_when_post() throws Exception {
        String requestBody = """
            {
                "name": "John Smith",
                "age": 32,
                "gender": "Male",
                "salary": 5000.0        
            }
        """;

        MockHttpServletRequestBuilder request = post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }

    @Test
    public void should_return_employee_when_get_employee_with_id_exist() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee expectEmployee = employeeController.create(employee);
        MockHttpServletRequestBuilder request = get("/employees/" + expectEmployee.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectEmployee.id()))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }

    @Test
    public void should_return_males_when_list_by_male() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee employee2 = new Employee(null, "Lily", 22, "Female", 5000.0);
        Employee expectEmployee = employeeController.create(employee);
        Employee expectEmployee2 = employeeController.create(employee2);

        MockHttpServletRequestBuilder request = get("/employees?gender=male")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expectEmployee.id()))
                .andExpect(jsonPath("$[0].name").value(expectEmployee.name()))
                .andExpect(jsonPath("$[0].age").value(expectEmployee.age()))
                .andExpect(jsonPath("$[0].gender").value(expectEmployee.gender()))
                .andExpect(jsonPath("$[0].salary").value(expectEmployee.salary()));
    }

    @Test
    public void should_return_all_employees_when_get_employees() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee employee2 = new Employee(null, "Lily", 22, "Female", 5000.0);
        Employee expectEmployee = employeeController.create(employee);
        Employee expectEmployee2 = employeeController.create(employee2);

        MockHttpServletRequestBuilder request = get("/employees")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void should_return_updated_employee_when_update_existing_employee() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee expectEmployee = employeeController.create(employee);
        String requestBody = """
            {
                "name": "John Smith",
                "age": 33,
                "gender": "Male",
                "salary": 6000.0        
            }
        """;

        MockHttpServletRequestBuilder request = put("/employees/" + expectEmployee.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectEmployee.id()))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(33))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(6000.0));
    }

    @Test
    public void should_return_deleted_employee_when_delete_existing_employee() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee expectEmployee = employeeController.create(employee);

        MockHttpServletRequestBuilder request = delete("/employees/" + expectEmployee.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isNoContent());

        assertEquals(0, employeeController.getAllEmployees(null).size());
    }

}
