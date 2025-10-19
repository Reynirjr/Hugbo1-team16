package org.example.oops;

import jakarta.persistence.*;

@Entity
@Table(name = "opening_hours")
public class OpeningHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String weekday; // MONDAY, TUESDAY, etc.

    @Column(name = "open_time")
    private String openTime; // e.g. "08:00"

    @Column(name = "close_time")
    private String closeTime; // e.g. "17:00"

    // 🟢 Add all getters and setters here:

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
