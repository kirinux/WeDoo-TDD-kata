package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.domain.Amount;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

class DistributionTest {

    @Test
    void distribution_should_be_valid_by_default() {
        Distribution giftDistribution = Distribution.newGiftDistribution(Amount.of(100), LocalDate.now().minusDays(10));
        assertThat(giftDistribution.isValid()).isTrue();
        assertThat(giftDistribution.getType()).isEqualTo(Wallet.Type.GIFT);
    }

    @Test
    void distribution_should_be_valid_at_first_day_included() {
        Distribution giftDistribution = Distribution.newGiftDistributionStartToday(Amount.of(100));
        assertThat(giftDistribution.isValid()).isTrue();
    }

    @Test
    void distribution_should_be_valid_at_last_day_include() {
        Distribution giftDistribution = Distribution.newGiftDistribution(Amount.of(100), LocalDate.now().minusDays(Distribution.DISTRIBUTION_DURATION_IN_DAYS - 1));
        assertThat(giftDistribution.isValid()).isTrue();
    }

    @Test
    void distribution_should_be_invalid_after_last_day() {
        Distribution giftDistribution = Distribution.newGiftDistribution(Amount.of(100), LocalDate.now().minusDays(Distribution.DISTRIBUTION_DURATION_IN_DAYS));
        assertThat(giftDistribution.isValid()).isFalse();
    }

    @Test
    void distribution_should_be_invalid_before_start_date() {
        Distribution giftDistribution = Distribution.newGiftDistribution(Amount.of(100), LocalDate.now().plusDays(1));
        assertThat(giftDistribution.isValid()).isFalse();
    }

    @Test
    void distribution_end_date_should_be_equals_to_start_date_plus_duration() {
        Distribution giftDistribution = Distribution.newGiftDistributionStartToday(Amount.of(100));
        assertThat(giftDistribution.getEndDate()).isEqualTo(giftDistribution.getStartDate().plusDays(Distribution.DISTRIBUTION_DURATION_IN_DAYS - 1));
    }

    @Test
    void food_distribution_should_be_valid_until_next_year_february() {
        Distribution foodDistribution = Distribution.newFoodDistribution(Amount.of(100), LocalDate.now());
        assertThat(foodDistribution.isValid()).isTrue();
        assertThat(foodDistribution.getEndDate())
                .isEqualTo(LocalDate.of(foodDistribution.getStartDate().getYear() + 1, 2, 1).with(TemporalAdjusters.lastDayOfMonth()));

    }

    @Test
    void food_distribution_should_be_invalid_until_next_year_february() {
        Distribution foodDistribution = Distribution.newFoodDistribution(Amount.of(100), LocalDate.of(2020, 9, 1));
        assertThat(foodDistribution.isValid()).isFalse();
        assertThat(foodDistribution.getEndDate())
                .isEqualTo(LocalDate.of(foodDistribution.getStartDate().getYear() + 1, 2, 1).with(TemporalAdjusters.lastDayOfMonth()));
    }

}