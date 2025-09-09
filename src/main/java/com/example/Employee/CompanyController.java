package com.example.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Integer id) {
        return companies.stream().filter(company -> company.id().equals(id)).findFirst().orElse(null);
    }

    @PutMapping("/{id}")
    public Company updateCompanyById(@PathVariable Integer id, @RequestBody Company company) {
        Company existingCompany = companies.stream().filter(comp -> comp.id().equals(id)).findFirst().orElse(null);
        if (existingCompany == null) {
            return null;
        }

        Company updatedCompany = new Company(existingCompany.id(), company.name());
        companies.remove(existingCompany);
        companies.add(updatedCompany);
        return  updatedCompany;
    }

}
