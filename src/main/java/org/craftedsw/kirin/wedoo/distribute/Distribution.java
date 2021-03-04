package org.craftedsw.kirin.wedoo.distribute;

import lombok.Getter;
import lombok.ToString;
import org.craftedsw.kirin.wedoo.domain.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

@ToString
@Getter
public class Distribution {

    public static final int DISTRIBUTION_DURATION_IN_DAYS = 365;
    private static final Logger LOGGER = LoggerFactory.getLogger(Distribution.class);
    private static int DISTRIBUTION_ID_SEQ = 1;
    private final Wallet.Type type;
    private final Amount amount;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private long id;
    private Long endowmentId;
    private Long companyId;

    protected Distribution(long id, Wallet.Type type, Amount amount, LocalDate startDate) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = type.computeEndDate(startDate);
        LOGGER.debug("Distribution created: {}", this);
    }

    public static Distribution newGiftDistribution(Amount amount, LocalDate startDate) {
        return newDistribution(Wallet.Type.GIFT, amount, startDate);
    }

    public static Distribution newFoodDistribution(Amount amount, LocalDate startDate) {
        return newDistribution(Wallet.Type.FOOD, amount, startDate);
    }

    public static Distribution newGiftDistributionStartToday(Amount amount) {
        return newDistribution(Wallet.Type.GIFT, amount, LocalDate.now());
    }

    public static Distribution newFoodDistributionStartToday(Amount amount) {
        return newDistribution(Wallet.Type.FOOD, amount, LocalDate.now());
    }

    public static Distribution newDistribution(Wallet.Type type, Amount amount, LocalDate startDate) {
        return new Distribution(getNextId(), type, amount, startDate);
    }

    public static Distribution newDistributionStartToday(Wallet.Type type, Amount amount) {
        return newDistribution(type, amount, LocalDate.now());
    }

    private static int getNextId() {
        return DISTRIBUTION_ID_SEQ++;
    }

    public void register(Company company, Endowment endowment) {
        this.companyId = company.getId();
        this.endowmentId = endowment.getId();
    }

    public boolean isValid() {
        LocalDate today = LocalDate.now();
        return today.isAfter(getStartDate().minusDays(1)) && today.isBefore(endDate.plusDays(1));
    }

    public Optional<Long> getCompanyId() {
        return Optional.ofNullable(this.companyId);
    }

    public Optional<Long> getEndowmentId() {
        return Optional.ofNullable(this.endowmentId);
    }


}
