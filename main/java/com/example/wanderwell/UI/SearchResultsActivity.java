package com.example.wanderwell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderwell.R;
import com.example.wanderwell.Search.SearchResultAdapter;
import com.example.wanderwell.database.Repository;
import com.example.wanderwell.model.SearchViewModel;
import com.example.wanderwell.model.SearchViewModelFactory;

public class SearchResultsActivity extends AppCompatActivity {

    private SearchViewModel viewModel;
    private SearchResultAdapter adapter;
    private RecyclerView searchResultsRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        initViews();
        setupRecyclerView();
        setupViewModel();
        handleIntentAndSearch();
    }

    private void initViews() {
        searchResultsRecyclerView = findViewById(R.id.recyclerViewSearchResults);
    }

    private void setupRecyclerView() {
        adapter = new SearchResultAdapter(result -> {
            Intent intent;
            if ("vacation".equalsIgnoreCase(result.getType())) {
                intent = new Intent(this, VacationDetails.class);
                intent.putExtra("vacationId", result.getId());
            } else if ("excursion".equalsIgnoreCase(result.getType())) {
                intent = new Intent(this, ExcursionDetails.class);
                intent.putExtra("excursionId", result.getId());
            } else {
                return; // unknown type
            }
            startActivity(intent);
        });

        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(adapter);
        searchResultsRecyclerView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_fade_in)
        );
    }

    private void setupViewModel() {
        Repository repository = Repository.getInstance(getApplication());
        SearchViewModelFactory factory = new SearchViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);
    }

    private void handleIntentAndSearch() {
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");
        if (query == null) query = "";

        viewModel.searchAll(query).observe(this, results -> {
            adapter.setResults(results);
            searchResultsRecyclerView.scheduleLayoutAnimation();
        });
    }
}
