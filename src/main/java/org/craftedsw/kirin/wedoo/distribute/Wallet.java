package org.craftedsw.kirin.wedoo.distribute;

import lombok.Value;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

@Value
public class Wallet {

    public static final int DISTRIBUTION_DURATION_IN_DAYS = 365;

    private final long id;
    private final String name;
    private final Type type;

    public enum Type {
        GIFT {
            @Override
            public LocalDate computeEndDate(LocalDate startDate) {
                return startDate.plusDays(DISTRIBUTION_DURATION_IN_DAYS - 1);
            }
        }, FOOD {
            @Override
            public LocalDate computeEndDate(LocalDate startDate) {
                return LocalDate.of(
                        startDate.getYear() + 1,
                        Month.FEBRUARY,
                        1
                ).with(TemporalAdjusters.lastDayOfMonth());
            }
        };

        public abstract LocalDate computeEndDate(LocalDate startDate);
    }
}
