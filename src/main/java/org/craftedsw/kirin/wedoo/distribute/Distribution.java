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
public final class Distribution {

    public static final int DISTRIBUTION_DURATION_IN_DAYS = 365;
    private static final Logger LOGGER = LoggerFactory.getLogger(Distribution.class);
    private static int DISTRIBUTION_ID_SEQ = 1;
    private final Amount amount;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private long id;
    private Long endowmentId;
    private Long companyId;

    private Distribution(long id, Amount amount, LocalDate startDate) {
        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(DISTRIBUTION_DURATION_IN_DAYS - 1);
        LOGGER.debug("Distribution created: {}", this);
    }

    public static Distribution newDistribution(Amount amount, LocalDate startDate) {
        return new Distribution(getNextId(), amount, startDate);
    }

    public static Distribution newDistributionStartToday(Amount amount) {
        return newDistribution(amount, LocalDate.now());
    }

    public void register(Company company, Endowment endowment) {
        this.companyId = company.getId();
        this.endowmentId = endowment.getId();
    }

    private static int getNextId() {
        return DISTRIBUTION_ID_SEQ++;
    }

    public long getId() {
        return id;
    }

    public Optional<Long> getCompanyId() {
        return Optional.ofNullable(this.companyId);
    }

    public Optional<Long> getEndowmentId() {
        return Optional.ofNullable(this.endowmentId);
    }

    public boolean isValid() {
        LocalDate today = LocalDate.now();
        return today.isAfter(startDate.minusDays(1)) && today.isBefore(endDate.plusDays(1));
    }

    public Amount getAmount() {
        return amount;
    }
}
