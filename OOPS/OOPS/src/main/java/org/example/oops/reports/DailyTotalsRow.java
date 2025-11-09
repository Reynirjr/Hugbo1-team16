package org.example.oops.reports;

import java.time.LocalDate;

public interface DailyTotalsRow {
    LocalDate getDay();
    long getOrdersCount();
    long getTotalRevenue();
}
