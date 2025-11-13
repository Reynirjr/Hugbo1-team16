package org.example.oops;

import org.example.oops.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
public class SalesReportService {

    private final ReportRepository reportRepo;

    public SalesReportService(ReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    public record DailyRow(LocalDate day, long ordersCount, long totalRevenueIsk, long avgOrderIsk) {}
    public record ItemRow(Integer itemId, String itemName, long qty, long revenueIsk) {}

    public static class SalesReport {
        public List<DailyRow> days;
        public List<ItemRow> items;
        public long grandTotalIsk;
        public long ordersTotal;
        public SalesReport(List<DailyRow> d, List<ItemRow> i) {
            this.days = d;
            this.items = i;
            this.grandTotalIsk = d.stream().mapToLong(r -> r.totalRevenueIsk).sum();
            this.ordersTotal    = d.stream().mapToLong(r -> r.ordersCount).sum();
        }
    }

    public SalesReport getReport(Instant from, Instant to, ZoneId zoneId) {
        String tz = zoneId.getId();

        var days = reportRepo.dailyTotals(from, to, tz).stream()
                .map(r -> {
                    var day = r.getDay();
                    long orders = r.getOrdersCount();
                    long revenue = r.getTotalRevenue();
                    long avg = orders > 0 ? Math.round((double) revenue / orders) : 0;
                    return new DailyRow(day, orders, revenue, avg);
                }).toList();

        var items = reportRepo.itemBreakdown(from, to).stream()
                .map(r -> new ItemRow(r.getItemId(), r.getItemName(), r.getQty(), r.getRevenue()))
                .toList();

        return new SalesReport(days, items);
    }

    public static Instant startOfDay(LocalDate date, ZoneId zone) {
        return date.atStartOfDay(zone).toInstant();
    }
    public static Instant startOfNextDay(LocalDate date, ZoneId zone) {
        return date.plusDays(1).atStartOfDay(zone).toInstant();
    }

    private static long getLong(Object o) {
        if (o == null) return 0;
        if (o instanceof Number n) return n.longValue();
        return Long.parseLong(o.toString());
    }

    public byte[] toCsv(SalesReport r, ZoneId zone) {
        StringBuilder sb = new StringBuilder();
        sb.append("Daily Totals\n");
        sb.append("day,orders,total_isk,avg_order_isk\n");
        for (var d : r.days) {
            sb.append(d.day()).append(",")
                    .append(d.ordersCount()).append(",")
                    .append(d.totalRevenueIsk()).append(",")
                    .append(d.avgOrderIsk()).append("\n");
        }
        sb.append("\nItem Breakdown\n");
        sb.append("item_id,item_name,qty,revenue_isk\n");
        for (var it : r.items) {
            sb.append(Objects.toString(it.itemId(), ""))
                    .append(",\"").append(escapeCsv(it.itemName())).append("\",")
                    .append(it.qty()).append(",")
                    .append(it.revenueIsk()).append("\n");
        }
        sb.append("\nGrand Totals,,orders:").append(r.ordersTotal)
                .append(",revenue_isk:").append(r.grandTotalIsk).append("\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"");
    }
}
