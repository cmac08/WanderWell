package com.example.wanderwell.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.wanderwell.database.DateTypeConverter;

import java.util.Date;

@Entity(tableName = "excursions")
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionId;
    private String excursionName;
    @TypeConverters({DateTypeConverter.class})
    private Date excursionDate;
    private int vacationId;

    // Constructor for inserting new excursions (no ID)
    public Excursion(String excursionName, Date excursionDate, int vacationId) {
        this.excursionName = excursionName;
        this.excursionDate = excursionDate;
        this.vacationId = vacationId;
    }

    // Constructor with ID (for reading or testing)
    @Ignore
    public Excursion(int excursionId, String excursionName, Date excursionDate, int vacationId) {
        this.excursionId = excursionId;
        this.excursionName = excursionName;
        this.excursionDate = excursionDate;
        this.vacationId = vacationId;
    }

    // Getters and Setters
    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public Date getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(Date excursionDate) {
        this.excursionDate = excursionDate;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}
