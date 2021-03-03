package org.craftedsw.kirin.wedoo.distribute;

import com.github.javafaker.Faker;
import org.craftedsw.kirin.wedoo.domain.Amount;
import org.craftedsw.kirin.wedoo.domain.Balance;

import java.time.LocalDate;

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

    public EndowmentTestBuilder withValidDistributions(double... amounts) {
        for (double d : amounts) {
            this.endowment.receive(Distribution.newDistribution(Amount.of(d), startDate));
        }
        return this;
    }

    public EndowmentTestBuilder withInvalidDistributions(double... amounts) {
        for (double d : amounts) {
            this.endowment.receive(Distribution.newDistribution(Amount.of(d), startDate.minusDays(Distribution.DISTRIBUTION_DURATION_IN_DAYS + 1)));
        }
        return this;
    }

    public Endowment build() {
        return endowment;
    }

    public EndowmentTestBuilder newEndownment(double initialBalance) {
        this.endowment = new Endowment(faker.number().randomNumber(2, false), Balance.of(initialBalance));
        return this;
    }
}
