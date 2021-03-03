package org.craftedsw.kirin.wedoo.distribute.infra;

import org.craftedsw.kirin.wedoo.distribute.Company;
import org.craftedsw.kirin.wedoo.distribute.CompanyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryCompanyRepository implements CompanyRepository {

    private final Map<String, Company> companyMap = new HashMap<>();

    @Override
    public Optional<Company> getCompany(String name) {
        return Optional.ofNullable(companyMap.get(name));
    }

    @Override
    public Company addNewCompany(Company company) {
        if (companyMap.containsKey(company.getName())) {
            throw new CompanyAlreadyExistsException(company);
        }
        return companyMap.put(company.getName(), company);
    }

    @Override
    public List<Company> getAll() {
        return companyMap.values().stream()
                .collect(Collectors.toUnmodifiableList());
    }
}
