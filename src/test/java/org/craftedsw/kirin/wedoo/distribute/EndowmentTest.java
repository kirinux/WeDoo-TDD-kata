package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.domain.Amount;
import org.craftedsw.kirin.wedoo.domain.Balance;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class EndowmentTest {

    @Test
    void valid_distribution_receiving_should_alter_endowment_balance() {
        Distribution distribution = Distribution.newDistributionStartToday(Amount.of(100));
        Balance initialBalance = Balance.of(10);
        Endowment endowment = new Endowment(2, initialBalance);

        assertThatCode(() -> endowment.receive(distribution))
                .doesNotThrowAnyException();
        assertThat(endowment.getBalance()).isEqualTo(initialBalance.plus(distribution.getAmount()));
    }

    @Test
    void endowment_should_store_all_distributions() {
        Endowment endowment = EndowmentTestBuilder.newEndowment()
                .newEndownment(0)
                .withValidDistributions(50, 10, 40)
                .withInvalidDistributions(10)
                .build();

        assertThat(endowment.getDistributions()).hasSize(4);
    }

    @Test
    void endowment_balance_should_contains_only_valid_distribution() {
        Endowment endowment = EndowmentTestBuilder.newEndowment()
                .newEndownment(0)
                .withValidDistributions(50, 10, 40)
                .withInvalidDistributions(10)
                .build();

        assertThat(endowment.getBalance().getValue()).isEqualTo(100);

    }

}