package org.craftedsw.kirin.wedoo.distribute;

import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@ToString
@Getter
public class Endowment {

    private static final Logger LOGGER = LoggerFactory.getLogger(Endowment.class);

    private final long id;
    private Balances balances;

    public Endowment(long id) {
        this(id, Balances.initialBalances());
    }

    public Endowment(long id, Balances initialBalances) {
        this.id = id;
        this.balances = initialBalances;
    }

    public void receive(Distribution distribution) {
        LOGGER.debug("receive distribution amount {}", distribution.getAmount());
        this.balances.add(distribution);
        LOGGER.debug("balance after distribution: {}", balances);
    }

    public Balances getBalances() {
        return balances;
    }

    public List<Distribution> getGiftDistributions() {
        return this.balances.getDistributionsByType(Wallet.Type.GIFT);
    }

    public List<Distribution> getFoodDistributions() {
        return this.balances.getDistributionsByType(Wallet.Type.FOOD);
    }


}
