package org.craftedsw.kirin.wedoo.distribute;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CompanyTest {

    @Test
    void company_should_not_contains_duplicate_endowments() {
        long duplicateId = 1;
        Endowment e1 = new Endowment(duplicateId);
        Endowment e2 = new Endowment(duplicateId);

        Company company = CompanyTestBuilder.newCompany().build();
        company.addEndowment(e1);

        assertThatExceptionOfType(EndowmentAlreadyExistsException.class)
                .isThrownBy(() -> company.addEndowment(e2));

    }

}