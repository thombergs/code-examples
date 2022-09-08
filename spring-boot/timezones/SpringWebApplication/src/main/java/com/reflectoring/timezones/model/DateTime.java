package com.reflectoring.timezones.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.*;
import java.util.Date;

public class DateTime {

    private Integer id;

    @JsonProperty("dateInStr (column date_str)")
    private String dateInStr;

    @JsonProperty("util dateInDefaultFormat (column date_time)")
    private Date dateInDefaultFormat;

    @JsonProperty("localDate (column local_date)")
    private LocalDate localDate;

    @JsonProperty("localTime (column local_time)")
    private LocalTime localTime;

    @JsonProperty("localDateTime (column local_datetime_dt)")
    private LocalDateTime localDateTime;

    @JsonProperty("offsetDateTime (column offset_datetime)")
    private OffsetDateTime offsetDateTime;

    @JsonProperty("zonedDateTime (column zoned_datetime)")
    private ZonedDateTime zonedDateTime;

    @JsonProperty("createdDateTime (column created_at)")
    private OffsetDateTime createdDateTime;

    private String applicationTimezone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateInStr() {
        return dateInStr;
    }

    public void setDateInStr(String dateInStr) {
        this.dateInStr = dateInStr;
    }

    public Date getDateInDefaultFormat() {
        return dateInDefaultFormat;
    }

    public void setDateInDefaultFormat(Date dateInDefaultFormat) {
        this.dateInDefaultFormat = dateInDefaultFormat;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public OffsetDateTime getOffsetDateTime() {
        return offsetDateTime;
    }

    public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
        this.offsetDateTime = offsetDateTime;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public String getApplicationTimezone() {
        return applicationTimezone;
    }

    public void setApplicationTimezone(String applicationTimezone) {
        this.applicationTimezone = applicationTimezone;
    }

    public OffsetDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(OffsetDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        return "DateTime{" +
                "id=" + id +
                ", dateInStr='" + dateInStr + '\'' +
                ", dateInDefaultFormat=" + dateInDefaultFormat +
                ", localDate=" + localDate +
                ", localTime=" + localTime +
                ", localDateTime=" + localDateTime +
                ", offsetDateTime=" + offsetDateTime +
                ", zonedDateTime=" + zonedDateTime +
                ", createdDateTime=" + createdDateTime +
                ", applicationTimezone='" + applicationTimezone + '\'' +
                '}';
    }
}
