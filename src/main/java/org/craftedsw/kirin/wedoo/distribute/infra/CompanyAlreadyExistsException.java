package org.craftedsw.kirin.wedoo.distribute.infra;

import org.craftedsw.kirin.wedoo.distribute.Company;

public class CompanyAlreadyExistsException extends RuntimeException {

    public CompanyAlreadyExistsException(Company company) {
        super(String.format("Company with name %s already exists", company.getName()));
    }
}
