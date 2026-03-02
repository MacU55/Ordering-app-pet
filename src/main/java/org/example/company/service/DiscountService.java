package org.example.company.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@ConditionalOnProperty(name = "company.discount.enabled", havingValue = "true")
public class DiscountService {

    private static final int DAYS_IN_WEEK_OPERATIONAL = 6;
    private static final int LAST_WEEK_DISCOUNT_FACTOR = 2;

    public BigDecimal calculateDiscount(double baseDiscountValue) {
        double value = this.isLastWeekOfMonth(LocalDate.now()) ? baseDiscountValue * LAST_WEEK_DISCOUNT_FACTOR : baseDiscountValue;
        return BigDecimal.valueOf(value);
    }

    private boolean isLastWeekOfMonth(LocalDate date) {
        var lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        var startOfLastWeek = lastDay.minusDays(DAYS_IN_WEEK_OPERATIONAL);
        return !date.isBefore(startOfLastWeek);
    }

}
