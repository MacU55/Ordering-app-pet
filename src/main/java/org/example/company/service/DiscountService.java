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

    public BigDecimal calculateDiscount(double taxValue) {
        double value = this.isLastWeekOfMonth(LocalDate.now()) ? taxValue * 2 : taxValue;
        return BigDecimal.valueOf(value);
    }

    private boolean isLastWeekOfMonth(LocalDate date) {
        var lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        var startOfLastWeek = lastDay.minusDays(DAYS_IN_WEEK_OPERATIONAL);
        return !date.isBefore(startOfLastWeek);
    }

}
