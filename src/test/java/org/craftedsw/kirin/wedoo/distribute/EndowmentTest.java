package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.domain.Amount;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class EndowmentTest {

    @Test
    void valid_distribution_receiving_should_alter_endowment_balance() {
        Distribution giftDistribution = Distribution.newGiftDistributionStartToday(Amount.of(100));
        WalletBalance initialWalletBalance = WalletBalance.forGift(10);
        Endowment endowment = new Endowment(2, CompanyTestBuilder.balances(initialWalletBalance, initialWalletBalance));

        assertThatCode(() -> endowment.receive(giftDistribution))
                .doesNotThrowAnyException();
        assertThat(endowment.getBalances().giftBalance()).isEqualTo(initialWalletBalance.plus(giftDistribution.getAmount()));
    }

    @Test
    void endowment_should_store_all_distributions() {
        Endowment endowment = EndowmentTestBuilder.newEndowment()
                .newEndownment(0)
                .withValidGiftDistributions(50, 10, 40)
                .withInvalidGiftDistributions(10)
                .build();

        assertThat(endowment.getGiftDistributions()).hasSize(4);
    }

    @Test
    void endowment_balance_should_contains_only_valid_distribution() {
        Endowment endowment = EndowmentTestBuilder.newEndowment()
                .newEndownment(0)
                .withValidGiftDistributions(50, 10, 40)
                .withInvalidGiftDistributions(10)
                .build();

        assertThat(endowment.getBalances().giftBalance().getValue()).isEqualTo(100);

    }

    @Test
    void endowment_with_mixed_distribution_should_have_valid_balances() {
        Endowment endowment = new Endowment(1);

        endowment.receive(Distribution.newGiftDistributionStartToday(Amount.of(10)));
        endowment.receive(Distribution.newGiftDistributionStartToday(Amount.of(90)));
        endowment.receive(Distribution.newFoodDistributionStartToday(Amount.of(300)));

        assertThat(endowment.getBalances().giftBalance().getValue()).isEqualTo(100);
        assertThat(endowment.getBalances().foodBalance().getValue()).isEqualTo(300);
        assertThat(endowment.getBalances().getAll()).hasSize(2);

    }

}