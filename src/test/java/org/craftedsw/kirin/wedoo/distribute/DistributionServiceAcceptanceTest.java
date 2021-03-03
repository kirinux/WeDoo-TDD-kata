package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.domain.Amount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DistributionServiceAcceptanceTest {

    @Test
    void distribution_to_unknown_company_should_throw_exception() {
        DistributionService distributionService = DistributionServiceTestBuilder.newDistributionService()
                .withUnknownCompany()
                .build();

        assertThatExceptionOfType(ObjectNotFoundException.class)
                .isThrownBy(() -> distributionService.distribute(Distribution.newDistributionStartToday(Amount.of(30)),
                        "plop",
                        33));

    }

    @Test
    void distribution_from_company_without_enough_balance_should_throw_exception() {
        DistributionService distributionService = DistributionServiceTestBuilder.newDistributionService()
                .withEndowment(33, 0)
                .withCompanyWithBalance(10)
                .build();

        assertThatExceptionOfType(NotEnoughFundsException.class)
                .isThrownBy(() -> distributionService.distribute(Distribution.newDistributionStartToday(Amount.of(30)),
                        "plop",
                        33));

    }

    @Test
    void distributions_should_modify_company_and_endowments_balances() throws IOException {
        DistributionService distributionService = DistributionServiceTestBuilder.newDistributionService()
                .buildWithData("/Level1/data/input.json");

        String companyName = "Wedoogift";
        long endowmentId1 = 1;
        distributionService.distribute(Distribution.newDistributionStartToday(Amount.of(50)), companyName, endowmentId1);
        long endowmentId2 = 2;
        distributionService.distribute(Distribution.newDistributionStartToday(Amount.of(100)), companyName, endowmentId2);

        Optional<Company> companyOpt = distributionService.getCompany(companyName);
        Assertions.assertAll(
                () -> assertThat(companyOpt).isPresent(),
                () -> assertThat(companyOpt.get().getBalance().getValue()).isEqualTo(850)
        );

        Company wedoogift = companyOpt.get();
        assertEndowmentExistsAndHaveBalance(wedoogift, endowmentId1, 150);
        assertEndowmentExistsAndHaveBalance(wedoogift, endowmentId2, 100);


    }

    @Test
    void distributions_should_be_added_to_company_history() {
        String companyName = "Couca-coula";
        DistributionService distributionService = DistributionServiceTestBuilder.newDistributionService()
                .withEndowment(1, 0)
                .withHugeBalanceCompany(companyName)
                .build();

        Distribution distribution1 = Distribution.newDistributionStartToday(Amount.of(30));
        distributionService.distribute(distribution1, companyName, 1);
        Distribution distribution2 = Distribution.newDistributionStartToday(Amount.of(70));
        distributionService.distribute(distribution2, companyName, 1);

        Company coucaCoulaCompany = distributionService.getCompany(companyName).orElseThrow();
        assertThat(coucaCoulaCompany.getDistributions())
                .containsExactly(distribution1, distribution2)
                .allSatisfy((Distribution distribution) -> {
                    assertThat(distribution.getEndowmentId()).isPresent();
                    assertThat(distribution.getCompanyId()).isPresent();
                    assertThat(distribution.getId()).isNotNull();
                });

    }

    private void assertEndowmentExistsAndHaveBalance(Company company, long endowmentId, double expectedBalance) {
        Optional<Endowment> endowment1 = company.getEndowment(endowmentId);
        Assertions.assertAll(
                () -> assertThat(endowment1).isPresent(),
                () -> assertThat(endowment1.get().getBalance().getValue()).isEqualTo(expectedBalance)
        );
    }


}