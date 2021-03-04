package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.domain.Amount;
import org.craftedsw.kirin.wedoo.domain.Balance;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CompanyAcceptanceTest {


    @Test
    void company_without_enough_funds_should_not_be_able_to_make_distribution() {
        Balance initialBalance = Balance.of(10);
        long endowmentId = 3;
        Company company = CompanyTestBuilder.newCompany()
                .withEndowment(endowmentId)
                .withBalance(initialBalance)
                .build();

        Distribution giftDistribution = Distribution.newGiftDistributionStartToday(Amount.of(1000));
        assertThatExceptionOfType(NotEnoughFundsException.class)
                .isThrownBy(() -> company.distribute(getEndowment(company, endowmentId), giftDistribution));

    }

    @Test
    void company_with_enough_balance_should_make_distribution() {
        WalletBalance originalEndownentWalletBalance = WalletBalance.forGift(10);
        Balance initialCompanyWalletBalance = Balance.of(1000);
        long endowmentId = 3;
        Company company = CompanyTestBuilder.newCompany()
                .withEndowment(endowmentId, originalEndownentWalletBalance)
                .withBalance(initialCompanyWalletBalance)
                .build();

        Distribution giftDistribution = Distribution.newGiftDistributionStartToday(Amount.of(10));

        assertThatCode(() -> company.distribute(getEndowment(company, endowmentId), giftDistribution))
                .doesNotThrowAnyException();

        assertThat(company.getInitialBalance()).isEqualTo(initialCompanyWalletBalance.minus(giftDistribution.getAmount()));
        assertEndowmentExistsAndBalanceEquals(endowmentId, company, originalEndownentWalletBalance.plus(giftDistribution.getAmount()));
    }

    private Endowment getEndowment(Company testCompany, long endowmentId) {
        return testCompany.getEndowment(endowmentId).get();
    }

    private void assertEndowmentExistsAndBalanceEquals(long endowmentId, Company company, WalletBalance expectedWalletBalance) {
        Optional<Endowment> companyEndowment = company.getEndowment(endowmentId);
        assertThat(companyEndowment).isPresent();
        assertThat(companyEndowment.get().getBalances().giftBalance()).isEqualTo(expectedWalletBalance);
    }
}