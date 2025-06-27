package com.example.wanderwell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderwell.R;
import com.example.wanderwell.Search.SearchResultAdapter;
import com.example.wanderwell.database.Repository;
import com.example.wanderwell.entities.Excursion;
import com.example.wanderwell.entities.Vacation;
import com.example.wanderwell.model.SearchViewModel;
import com.example.wanderwell.model.SearchViewModelFactory;
import com.example.wanderwell.model.VacationListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VacationList extends AppCompatActivity {

    private SearchViewModel viewModel;
    private VacationListViewModel vacationListViewModel;
    private VacationAdapter vacationAdapter;
    private SearchResultAdapter adapter;
    private SearchBar searchBar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private Repository repository;
    TextView noVacationsMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        noVacationsMessage = findViewById(R.id.noVacationsMessage);
        searchBar = findViewById(R.id.searchBar);
        searchBar.setVisibility(View.VISIBLE);

        searchView = findViewById(R.id.searchView);

        // Show SearchView on click
        searchBar.setOnClickListener(v -> searchView.show());

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter = new VacationAdapter(this, Repository.getInstance(getApplication()), vacationListViewModel);
        recyclerView.setAdapter(vacationAdapter);

        // Other setup...
        repository = new Repository(getApplication());
        setupViewModel();
        loadVacationsAsync();
    }
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_vacation_list);
//        noVacationsMessage = findViewById(R.id.noVacationsMessage);
//
//
//        initViews();
//        repository = new Repository(getApplication());
//        setupRecyclerView();
//        setupViewModel();
////        setupSearchBar();
//        loadVacationsAsync();
//        searchBar.setOnClickListener(v -> searchView.show());
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVacationsAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.clear_data) {
            clearAllData();
            return true;
        }

        if (item.getItemId() == R.id.mysample) {
            Toast.makeText(this, "Sample menu item clicked", Toast.LENGTH_SHORT).show();
            try {
                insertSampleDataAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertSampleDataAsync() {
        Repository.databaseExecutor.execute(() -> {
            try {
                // Insert sample vacations
                Vacation vacationMexico = new Vacation("Mexico", "09/04/2025", "09/14/2025", "Marriott");
                Vacation vacationFrance = new Vacation("France", "10/01/2025", "10/11/2025", "Wyndham");

                repository.insert(vacationMexico);
                repository.insert(vacationFrance);

                Thread.sleep(300); // 300 milliseconds because Room is a little slow

                // After inserting, reload to get real IDs
                List<Vacation> vacations = repository.getAllVacationsSync();

                int mexicoId = -1;
                int franceId = -1;

                for (Vacation v : vacations) {
                    if (v.getVacationName().equals("Mexico")) {
                        mexicoId = v.getVacationId();
                    } else if (v.getVacationName().equals("France")) {
                        franceId = v.getVacationId();
                    }
                }
                // Only insert excursions if IDs found
                if (mexicoId != -1) {
                    Excursion cycling = new Excursion("Cycling", new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse("09/06/2025"), mexicoId);
                    repository.insert(cycling);
                }
                if (franceId != -1) {
                    Excursion wineTasting = new Excursion("Wine Tasting", new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse("10/05/2025"), franceId);
                    repository.insert(wineTasting);
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "Sample Data Inserted!", Toast.LENGTH_SHORT).show();
                    loadVacationsAsync();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initViews() {
        searchBar = findViewById(R.id.searchBar);
        searchView = findViewById(R.id.searchView);
        searchBar.setVisibility(View.VISIBLE);

//        vacationListViewModel.getAllVacations().observe(this, vacations -> {
//            vacationAdapter.setVacations(vacations);
//        });

    }

    private void setupRecyclerView() {
        vacationAdapter = new VacationAdapter(
            this,
            Repository.getInstance(getApplication()),
            vacationListViewModel
        );

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setFocusable(false);
        recyclerView.clearFocus();
    }

    private void setupViewModel() {
        Repository repository = Repository.getInstance(getApplication());
        SearchViewModelFactory factory = new SearchViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);
    }

    private void loadVacationsAsync() {
        Repository.databaseExecutor.execute(() -> {
            List<Vacation> allVacations = repository.getAllVacationsSync();
            runOnUiThread(() -> {
                vacationAdapter.setVacations(allVacations);

                if (allVacations.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noVacationsMessage.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noVacationsMessage.setVisibility(View.GONE);
                }
            });
        });
    }
    private void setupSearchBar() {
        // This connects the SearchBar toggle behavior with SearchView visibility
        searchBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.searchBar) {
                searchView.show(); // show search view when menu item is clicked
                return true;
            }
            return false;
        });

        searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no-op
            }
        });
    }
    private void performSearch(String query) {
        viewModel.searchAll(query).observe(this, results -> {
            if (results != null) {
                adapter.setResults(results);
            } else {
                adapter.setResults(null);
                Toast.makeText(this, "No results found.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clearAllData() {
        Repository.databaseExecutor.execute(() -> {
            List<Excursion> allExcursions = repository.getAllExcursionsSync();
            for (Excursion e : allExcursions) {
                repository.delete(e);
            }
            List<Vacation> allVacations = repository.getAllVacationsSync();
            for (Vacation v : allVacations) {
                repository.delete(v);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "All vacations and excursions deleted!", Toast.LENGTH_SHORT).show();
                loadVacationsAsync(); // Refresh the list
            });
        });
    }

}

