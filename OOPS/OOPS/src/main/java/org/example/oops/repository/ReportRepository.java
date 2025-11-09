package org.example.oops.repository;

import org.example.oops.Order;
import org.example.oops.reports.DailyTotalsRow;
import org.example.oops.reports.ItemBreakdownRow;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

@Profile("reports")
public interface ReportRepository extends Repository<Order, Integer> {

    @Query(value = """
      SELECT 
        (date_trunc('day', o.created_at AT TIME ZONE :tz))::date AS day,
        COUNT(*)                                  AS orders_count,
        COALESCE(SUM(o.total_isk), 0)             AS total_revenue
      FROM orders o
      WHERE o.created_at >= :from AND o.created_at < :to
      GROUP BY day
      ORDER BY day
      """, nativeQuery = true)
    List<DailyTotalsRow> dailyTotals(
            @Param("from") Instant from,
            @Param("to")   Instant to,
            @Param("tz")   String tz);

    @Query(value = """
      SELECT 
        oi.item_id                         AS item_id,
        oi.item_name                       AS item_name,
        COALESCE(SUM(oi.quantity), 0)      AS qty,
        COALESCE(SUM(oi.price_isk*oi.quantity), 0) AS revenue
      FROM orders o
      JOIN order_items oi ON oi.order_id = o.id
      WHERE o.created_at >= :from AND o.created_at < :to
      GROUP BY oi.item_id, oi.item_name
      ORDER BY revenue DESC, qty DESC
      """, nativeQuery = true)
    List<ItemBreakdownRow> itemBreakdown(
            @Param("from") Instant from,
            @Param("to")   Instant to);
}
