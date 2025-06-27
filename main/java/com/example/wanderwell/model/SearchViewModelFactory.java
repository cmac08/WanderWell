package com.example.wanderwell.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanderwell.database.Repository;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    private final Repository repository;

    public SearchViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
