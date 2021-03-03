package org.craftedsw.kirin.wedoo.distribute;

import org.craftedsw.kirin.wedoo.domain.Amount;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DistributionTest {

    @Test
    void distribution_should_be_valid_by_default() {
        Distribution distribution = Distribution.newDistribution(Amount.of(100), LocalDate.now().minusDays(10));
        assertThat(distribution.isValid()).isTrue();
    }

    @Test
    void distribution_should_be_valid_at_first_day_included() {
        Distribution distribution = Distribution.newDistributionStartToday(Amount.of(100));
        assertThat(distribution.isValid()).isTrue();
    }

    @Test
    void distribution_should_be_valid_at_last_day_exclude() {
        Distribution distribution = Distribution.newDistribution(Amount.of(100), LocalDate.now().minusDays(Distribution.DISTRIBUTION_DURATION_IN_DAYS - 1));
        assertThat(distribution.isValid()).isTrue();
    }

    @Test
    void distribution_should_be_invalid_at_last_day_include() {
        Distribution distribution = Distribution.newDistribution(Amount.of(100), LocalDate.now().minusDays(Distribution.DISTRIBUTION_DURATION_IN_DAYS));
        assertThat(distribution.isValid()).isFalse();
    }

    @Test
    void distribution_end_date_should_be_equals_to_start_date_plus_duration() {
        Distribution distribution = Distribution.newDistributionStartToday(Amount.of(100));
        assertThat(distribution.getEndDate()).isEqualTo(distribution.getStartDate().plusDays(Distribution.DISTRIBUTION_DURATION_IN_DAYS - 1));

    }

}