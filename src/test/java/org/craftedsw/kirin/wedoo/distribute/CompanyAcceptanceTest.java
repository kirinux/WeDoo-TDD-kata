package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.domain.Amount;
import org.craftedsw.kirin.wedoo.domain.Balance;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CompanyAcceptanceTest {


    @Test
    void company_without_enough_funds_should_not_be_able_to_make_distribution() {
        Balance initialCompanyBalance = Balance.of(10);
        long endowmentId = 3;
        Company company = CompanyTestBuilder.newCompany()
                .withEndowment(endowmentId)
                .withBalance(initialCompanyBalance)
                .build();

        Distribution distribution = Distribution.newDistributionStartToday(Amount.of(1000));
        assertThatExceptionOfType(NotEnoughFundsException.class)
                .isThrownBy(() -> company.distribute(getEndowment(company, endowmentId), distribution));

    }

    @Test
    void company_with_enough_balance_should_make_distribution() {
        Balance originalEndownentBalance = Balance.of(10);
        Balance initialCompanyBalance = Balance.of(1000);
        long endowmentId = 3;
        Company company = CompanyTestBuilder.newCompany()
                .withEndowment(endowmentId, originalEndownentBalance)
                .withBalance(initialCompanyBalance)
                .build();

        Distribution distribution = Distribution.newDistributionStartToday(Amount.of(10));

        assertThatCode(() -> company.distribute(getEndowment(company, endowmentId), distribution))
                .doesNotThrowAnyException();

        assertThat(company.getBalance()).isEqualTo(initialCompanyBalance.minus(distribution.getAmount()));
        assertEndowmentExistsAndBalanceEquals(endowmentId, company, originalEndownentBalance.plus(distribution.getAmount()));
    }

    private Endowment getEndowment(Company testCompany, long endowmentId) {
        return testCompany.getEndowment(endowmentId).get();
    }

    private void assertEndowmentExistsAndBalanceEquals(long endowmentId, Company company, Balance expectedBalance) {
        Optional<Endowment> companyEndowment = company.getEndowment(endowmentId);
        assertThat(companyEndowment).isPresent();
        assertThat(companyEndowment.get().getBalance()).isEqualTo(expectedBalance);
    }
}