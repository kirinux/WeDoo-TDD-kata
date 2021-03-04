package org.craftedsw.kirin.wedoo.distribute;

import com.github.javafaker.Faker;
import org.craftedsw.kirin.wedoo.domain.Amount;

import java.time.LocalDate;
import java.util.Map;

public final class EndowmentTestBuilder {

    private final Faker faker = new Faker();
    private Endowment endowment;
    private LocalDate startDate = LocalDate.now();

    private EndowmentTestBuilder() {
        this.endowment = new Endowment(faker.number().randomNumber(2, false));
    }

    public static EndowmentTestBuilder newEndowment() {
        return new EndowmentTestBuilder();
    }

    public EndowmentTestBuilder withValidGiftDistributions(double... amounts) {
        for (double d : amounts) {
            this.endowment.receive(Distribution.newGiftDistribution(Amount.of(d), startDate));
        }
        return this;
    }

    public EndowmentTestBuilder withValidFoodDistributions(double... amounts) {
        for (double d : amounts) {
            this.endowment.receive(Distribution.newFoodDistribution(Amount.of(d), startDate));
        }
        return this;
    }


    public EndowmentTestBuilder withInvalidGiftDistributions(double... amounts) {
        for (double d : amounts) {
            this.endowment.receive(Distribution.newGiftDistribution(Amount.of(d), startDate.minusDays(Distribution.DISTRIBUTION_DURATION_IN_DAYS + 1)));
        }
        return this;
    }

    public Endowment build() {
        return endowment;
    }

    public EndowmentTestBuilder newEndownment(double giftInitialBalance) {
        this.endowment = new Endowment(faker.number().randomNumber(2, false),
                Balances.initialBalances(
                        Map.of(
                                //TODO this piece of code should be shared
                                Wallet.Type.GIFT, WalletBalance.forGift(giftInitialBalance),
                                Wallet.Type.FOOD, WalletBalance.forGift(0)
                        )
                ));
        return this;
    }
}
