package org.craftedsw.kirin.wedoo.distribute;

import org.assertj.core.api.Assertions;
import org.craftedsw.kirin.wedoo.domain.Amount;
import org.junit.jupiter.api.Test;

import java.util.Map;

class BalancesTest {

    @Test
    void balances_should_have_good_type_and_correct_amount() {
        Balances balances = Balances.initialBalances(Map.of(Wallet.Type.GIFT, WalletBalance.forGift(0)));

        balances.add(Distribution.newGiftDistributionStartToday(Amount.of(10)));
        Assertions.assertThat(balances.giftBalance().getValue()).isEqualTo(10);
        Assertions.assertThat(balances.giftBalance().getType()).isEqualTo(Wallet.Type.GIFT);

        balances.add(Distribution.newFoodDistributionStartToday(Amount.of(200)));
        Assertions.assertThat(balances.foodBalance().getValue()).isEqualTo(200);
        Assertions.assertThat(balances.foodBalance().getType()).isEqualTo(Wallet.Type.FOOD);
    }

}