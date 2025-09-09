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

    public List<Company> getCompanies() {
        return companies;
    }

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
    public List<Company> getAllCompanies(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, companies.size());
            if (start >= 0 && start < companies.size()) {
                return companies.subList(start, end);
            }
        }

        return companies;
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Integer id) {
        return companies.stream().filter(company -> company.id().equals(id)).findFirst().orElse(null);
    }

    @PutMapping("/{id}")
    public Company updateCompanyById(@PathVariable Integer id, @RequestBody Company company) {
        Company existingCompany = this.getCompanyById(id);
        if (existingCompany == null) {
            return null;
        }

        Company updatedCompany = new Company(existingCompany.id(), company.name());
        companies.remove(existingCompany);
        companies.add(updatedCompany);
        return  updatedCompany;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompanyById(@PathVariable Integer id) {
        Company existingCompany = this.getCompanyById(id);
        companies.remove(existingCompany);
    }

}
