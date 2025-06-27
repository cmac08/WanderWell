package com.example.wanderwell.model;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderwell.database.Repository;
import com.example.wanderwell.entities.Excursion;
import com.example.wanderwell.entities.Vacation;

import java.util.List;

public class VacationListViewModel extends ViewModel {

    private final Repository repository;

    public VacationListViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return repository.getAllVacations();
    }

    public LiveData<List<Excursion>> getExcursionsForVacation(int vacationId) {
        return repository.getRelatedExcursionsById(vacationId);
    }
}
