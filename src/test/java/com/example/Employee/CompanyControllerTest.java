package com.example.Employee;

import jdk.jfr.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    public void setup() {
        companyController.clear();
    }

    @Test
    public void should_return_created_company_when_create_company_given_new_company() throws Exception {
        String requestBody = """
                {
                    "name": "company 1"
                }
                """;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("company 1"));
    }

    @Test
    public void should_return_all_companies_when_get_all_companies_given_existing_companies() throws Exception {
        Company company = new Company(null, "company 1");
        Company company2 = new Company(null, "company 2");
        companyController.createCompany(company);
        companyController.createCompany(company2);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/companies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void should_return_company_when_get_company_by_id_given_existing_company_id() throws Exception {
        Company company = new Company(null, "company 1");
        Company company2 = new Company(null, "company 2");
        companyController.createCompany(company);
        companyController.createCompany(company2);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/companies/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("company 2"));
    }

    @Test
    public void should_return_updated_company_when_update_company_given_new_company_name_and_existing_company_id() throws Exception {
        Company company = new Company(null, "company 1");
        companyController.createCompany(company);

        String requestBody = """
                {
                    "name": "company ABC"
                }
                """;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/companies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("company ABC"));
    }

    @Test
    public void should_return_deleted_company_when_delete_company_by_id_given_existing_company_id() throws Exception {
        Company company = new Company(null, "company 1");
        companyController.createCompany(company);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/companies/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    public void should_return_compaines_with_pagination_when_get_all_compaines_given_page_1_size_3() throws Exception {
        for (int i = 0; i < 10; i++) {
            companyController.createCompany(new Company(null, "Company " + i ));
        }

        MockHttpServletRequestBuilder request = get("/companies?page=1&size=3")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));
    }

    @Test
    public void should_return_companies_with_pagination_when_get_all_companies_given_page_2_size_5() throws Exception {
        for (int i = 0; i < 10; i++) {
            companyController.createCompany(new Company(null, "Company " + i ));
        }

        MockHttpServletRequestBuilder request = get("/companies?page=2&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(6))
                .andExpect(jsonPath("$[1].id").value(7))
                .andExpect(jsonPath("$[2].id").value(8))
                .andExpect(jsonPath("$[3].id").value(9))
                .andExpect(jsonPath("$[4].id").value(10));
    }

}
