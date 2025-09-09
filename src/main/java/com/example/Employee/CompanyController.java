package com.example.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private int id = 0;

    private List<Company> companies = new ArrayList<>();

    public void clear() {
        companies.clear();
        id = 0;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@RequestBody  Company company) {
        Company addedCompany = new Company(++id, company.name());
        companies.add(addedCompany);
        return addedCompany;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companies;
    }

}
