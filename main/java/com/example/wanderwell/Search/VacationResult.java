package com.example.wanderwell.Search;

import com.example.wanderwell.entities.Vacation;

public class VacationResult extends SearchResult {
    public int vacationId;
    public Vacation vacation;
    public VacationResult(Vacation vacation) {
        this.vacation = vacation;
        this.vacationId = vacation.getVacationId();
        this.title = vacation.getVacationName();
        this.type = "Vacation";
        this.dateInfo = vacation.getStartDate() + " - " + vacation.getEndDate();
        this.hotelName = vacation.getHotelName();
    }

    @Override
    public int getId() {
        return vacationId;
    }
}
