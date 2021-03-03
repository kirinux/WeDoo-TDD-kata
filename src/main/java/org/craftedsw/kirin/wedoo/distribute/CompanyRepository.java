package org.craftedsw.kirin.wedoo.distribute;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {

    Optional<Company> getCompany(String name);

    Company addNewCompany(Company company);

    List<Company> getAll(); //could be problematic with huge list
}
