import org.example.oops.SalesReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final SalesReportService service;

    public ReportsController(SalesReportService service) {
        this.service = service;
    }

    private ZoneId zone(String tz) {
        return (tz == null || tz.isBlank()) ? ZoneId.systemDefault() : ZoneId.of(tz);
    }

    @PreAuthorize("hasRole('SUPERUSER')")
    @GetMapping("/sales")
    public Map<String,Object> salesRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String tz) {

        ZoneId z = zone(tz);
        LocalDate f = (from == null) ? LocalDate.now(z).withDayOfMonth(1) : from;
        LocalDate t = (to   == null) ? LocalDate.now(z).plusDays(1) : to.plusDays(1); // exclusive

        var report = service.getReport(
                SalesReportService.startOfDay(f, z),
                SalesReportService.startOfDay(t, z),
                z);

        return Map.of(
                "from", f,
                "toExclusive", t,
                "timezone", z.getId(),
                "ordersTotal", report.ordersTotal,
                "grandTotalIsk", report.grandTotalIsk,
                "days", report.days,
                "items", report.items
        );
    }

    // STAFF/SUPERUSER: svo staff geti séð sölu dagsins og gærdagsins
    @PreAuthorize("hasAnyRole('SUPERUSER','STAFF')")
    @GetMapping(value = "/sales", params = "scope")
    public Map<String,Object> salesScoped(@RequestParam String scope,
                                          @RequestParam(required = false) String tz) {
        ZoneId z = zone(tz);
        LocalDate today = LocalDate.now(z);
        LocalDate target = switch (scope.toLowerCase()) {
            case "yesterday" -> today.minusDays(1);
            case "today" -> today;
            default -> today;
        };

        var report = service.getReport(
                SalesReportService.startOfDay(target, z),
                SalesReportService.startOfNextDay(target, z),
                z
        );

        return Map.of(
                "scope", scope.toLowerCase(),
                "day", target,
                "timezone", z.getId(),
                "ordersTotal", report.ordersTotal,
                "grandTotalIsk", report.grandTotalIsk,
                "days", report.days,
                "items", report.items
        );
    }

    // csv export fyrir einungis SupahUsah , hugsa það verði gott fyrir framendann í hugbó 2
    @PreAuthorize("hasRole('SUPERUSER')")
    @GetMapping(value = "/sales.csv")
    public ResponseEntity<byte[]> salesCsv(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String tz) {

        ZoneId z = zone(tz);
        var report = service.getReport(
                SalesReportService.startOfDay(from, z),
                SalesReportService.startOfNextDay(to, z),
                z
        );
        byte[] csv = service.toCsv(report, z);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"sales_" + from + "_to_" + to + ".csv\"");

        return new ResponseEntity<>(csv, headers, HttpStatus.OK);
    }
}
