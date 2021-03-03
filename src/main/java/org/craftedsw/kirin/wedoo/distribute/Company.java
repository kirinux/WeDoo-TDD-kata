package org.craftedsw.kirin.wedoo.distribute;

import lombok.Getter;
import org.craftedsw.kirin.wedoo.domain.Balance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Company {

    private static final Logger LOGGER = LoggerFactory.getLogger(Company.class);

    @Getter
    private final long id;
    @Getter
    private final String name;
    private final EndowmentRepository endowmentRepository;
    private final List<Distribution> distributions;
    private Balance balance;

    public Company(long id, String name, Balance initialBalance, EndowmentRepository endowmentRepository) {
        this.id = id;
        this.name = name;
        this.endowmentRepository = endowmentRepository;
        this.balance = initialBalance;
        this.distributions = new ArrayList<>();
    }

    public void distribute(@NotNull Endowment endowment, @NotNull Distribution distribution) {
        LOGGER.info("Distribute {} to endowment {}", distribution, endowment.getId());
        if (notEnoughBalance(distribution)) {
            throw new NotEnoughFundsException(String.format("Distribution amount %s if greater than company balance %s", distribution.getAmount().getValue(), balance.getValue()));
        }

        distributeToEndowment(endowment, distribution);
        decreaseBalance(distribution);
    }

    public Optional<Endowment> getEndowment(long endowmentId) {
        return endowmentRepository.getEndowment(endowmentId);
    }

    public void addEndowment(Endowment endowment) {
        endowmentRepository.addEndowment(endowment);
    }

    private boolean notEnoughBalance(Distribution distribution) {
        return distribution.getAmount().isGreaterThan(balance.getValue());
    }

    private void distributeToEndowment(Endowment endowment, Distribution distribution) {
        endowment.receive(distribution);
        distribution.register(this, endowment);
        distributions.add(distribution);
    }

    private void decreaseBalance(Distribution distribution) {
        balance = balance.minus(distribution.getAmount());
        LOGGER.debug("Balance after distribution {}", balance);
    }

    public List<Distribution> getDistributions() {
        return Collections.unmodifiableList(distributions);
    }

    public Balance getBalance() {
        return balance;
    }
}
