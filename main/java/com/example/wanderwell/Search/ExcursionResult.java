package com.example.wanderwell.Search;

import com.example.wanderwell.entities.Excursion;

public class ExcursionResult extends SearchResult{
    public int excursionId;
    public int vacationId;
    public ExcursionResult(Excursion excursion) {
        this.excursionId = excursion.getExcursionId();
        this.vacationId = excursion.getVacationId();
        this.dateInfo = excursion.getExcursionDate().toString();
        this.title = excursion.getExcursionName();
        this.type = "Excursion";
    }

    @Override
    public int getId() {
        return excursionId;
    }
}
