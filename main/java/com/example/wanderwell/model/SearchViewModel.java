package com.example.wanderwell.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderwell.database.Repository;
import com.example.wanderwell.entities.Excursion;
import com.example.wanderwell.entities.Vacation;
import com.example.wanderwell.Search.SearchResult;
import com.example.wanderwell.Search.VacationResult;
import com.example.wanderwell.Search.ExcursionResult;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final Repository repository;

    public SearchViewModel(Repository repository) {
        this.repository = repository;
    }
    public LiveData<List<SearchResult>> searchAll(String query) {
        MediatorLiveData<List<SearchResult>> combinedResults = new MediatorLiveData<>();
        List<SearchResult> temp = new ArrayList<>();

        LiveData<List<Vacation>> vacationResults = repository.searchVacations(query);
        LiveData<List<Excursion>> excursionResults = repository.searchExcursions(query);

        combinedResults.addSource(vacationResults, vacations -> {
            temp.removeIf(r -> r instanceof VacationResult);
            if (vacations != null) {
                for (Vacation v : vacations) {
                    temp.add(new VacationResult(v));
                }
            }
            combinedResults.setValue(new ArrayList<>(temp));
        });

        combinedResults.addSource(excursionResults, excursions -> {
            temp.removeIf(r -> r instanceof ExcursionResult);
            if (excursions != null) {
                for (Excursion e : excursions) {
                    temp.add(new ExcursionResult(e));
                }
            }
            combinedResults.setValue(new ArrayList<>(temp));
        });

        return combinedResults;
    }
}
