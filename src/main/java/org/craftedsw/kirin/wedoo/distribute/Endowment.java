package org.craftedsw.kirin.wedoo.distribute;

import lombok.Getter;
import lombok.ToString;
import org.craftedsw.kirin.wedoo.domain.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ToString
@Getter
public class Endowment {

    private static final Logger LOGGER = LoggerFactory.getLogger(Endowment.class);

    private final long id;
    private final List<Distribution> distributions;
    private Balance initialBalance;

    public Endowment(long id) {
        this(id, Balance.of(0));
    }

    public Endowment(long id, Balance initialBalance) {
        this.id = id;
        this.initialBalance = initialBalance;
        this.distributions = new ArrayList<>();
    }

    public void receive(Distribution distribution) {
        LOGGER.debug("receive distribution amount {}", distribution.getAmount());
        this.distributions.add(distribution);
        LOGGER.debug("balance after distribution: {}", initialBalance);
    }

    public List<Distribution> getDistributions() {
        return Collections.unmodifiableList(distributions);
    }

    public Balance getBalance() {
        double sum = distributions.stream()
                .filter(Distribution::isValid)
                .mapToDouble(distribution -> distribution.getAmount().getValue())
                .sum();
        return Balance.of(sum + initialBalance.getValue());
    }
}
