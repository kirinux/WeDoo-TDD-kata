package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.domain.Amount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class GiftDistributionServiceAcceptanceTest {

    @Test
    void distribution_to_unknown_company_should_throw_exception() {
        DistributionService distributionService = DistributionServiceTestBuilder.newDistributionService()
                .withUnknownCompany()
                .build();

        assertThatExceptionOfType(ObjectNotFoundException.class)
                .isThrownBy(() -> distributionService.distribute(Distribution.newGiftDistributionStartToday(Amount.of(30)),
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
                .isThrownBy(() -> distributionService.distribute(Distribution.newGiftDistributionStartToday(Amount.of(30)),
                        "plop",
                        33));

    }

    @Test
    void distributions_should_modify_company_and_endowments_balances() throws IOException {
        DistributionService distributionService = DistributionServiceTestBuilder.newDistributionService()
                .buildWithData("/Level2/data/input.json");

        String companyName = "Wedoogift";
        long endowmentId1 = 1;
        distributionService.distribute(Distribution.newGiftDistributionStartToday(Amount.of(50)), companyName, endowmentId1);
        long endowmentId2 = 2;
        distributionService.distribute(Distribution.newGiftDistributionStartToday(Amount.of(100)), companyName, endowmentId2);

        Optional<Company> companyOpt = distributionService.getCompany(companyName);
        Assertions.assertAll(
                () -> assertThat(companyOpt).isPresent(),
                () -> assertThat(companyOpt.get().getInitialBalance().getValue()).isEqualTo(850)
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

        Distribution giftDistribution1 = Distribution.newGiftDistributionStartToday(Amount.of(30));
        distributionService.distribute(giftDistribution1, companyName, 1);
        Distribution giftDistribution2 = Distribution.newGiftDistributionStartToday(Amount.of(70));
        distributionService.distribute(giftDistribution2, companyName, 1);

        Company coucaCoulaCompany = distributionService.getCompany(companyName).orElseThrow();
        assertThat(coucaCoulaCompany.getGiftDistributions())
                .containsExactly(giftDistribution1, giftDistribution2)
                .allSatisfy((Distribution giftDistribution) -> {
                    assertThat(giftDistribution.getEndowmentId()).isPresent();
                    assertThat(giftDistribution.getCompanyId()).isPresent();
                    assertThat(giftDistribution.getId()).isNotNull();
                });

    }

    @Test
    void food_card_distribution_should_update_endowment_balance_with_wallet_type() {
        String companyName = "MyGroSoft";
        DistributionService distributionService = DistributionServiceTestBuilder.newDistributionService()
                .withEndowment(1, 0)
                .withHugeBalanceCompany(companyName)
                .withAllWallets()
                .build();

        Distribution giftDistribution = Distribution.newGiftDistributionStartToday(Amount.of(30));
        distributionService.distribute(giftDistribution, companyName, 1);

        Distribution foodGiftDistribution = Distribution.newFoodDistributionStartToday(Amount.of(50));
        distributionService.distribute(foodGiftDistribution, companyName, 1);

        Company company = distributionService.getCompany(companyName).orElseThrow();
        assertThat(company.getGiftDistributions()).containsOnly(giftDistribution);
        assertThat(company.getFoodDistributions()).containsOnly(foodGiftDistribution);

        Endowment endowment = company.getEndowment(1).orElseThrow();
        Balances balances = endowment.getBalances();

        WalletBalance giftWalletBalance = balances.giftBalance();
        assertThat(giftWalletBalance.getValue()).isEqualTo(30);
        assertThat(endowment.getGiftDistributions()).hasSize(1);

        WalletBalance foodWalletBalance = balances.foodBalance();
        assertThat(foodWalletBalance.getValue()).isEqualTo(50);
        assertThat(endowment.getFoodDistributions()).hasSize(1);

    }

    private void assertEndowmentExistsAndHaveBalance(Company company, long endowmentId, double expectedBalance) {
        Optional<Endowment> endowment1 = company.getEndowment(endowmentId);
        Assertions.assertAll(
                () -> assertThat(endowment1).isPresent(),
                () -> assertThat(endowment1.get().getBalances().giftBalance().getValue()).isEqualTo(expectedBalance)
        );
    }


}