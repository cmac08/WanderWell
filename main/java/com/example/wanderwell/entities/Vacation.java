package com.example.wanderwell.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationId;
    private String vacationName;
    private String hotelName;
    private String startDate;
    private String endDate;

    public Vacation(String vacationName, String startDate, String endDate, String hotelName) {
        this.vacationName = vacationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotelName = hotelName;
    }

    // ✅ Constructor with ID for reading or testing
    @Ignore
    public Vacation(int vacationId, String vacationName, String startDate, String endDate, String hotelName) {
        this.vacationId = vacationId;
        this.vacationName = vacationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotelName = hotelName;
    }

    // --- Getters and Setters ---
    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
}
