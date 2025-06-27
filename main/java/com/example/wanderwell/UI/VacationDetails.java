package com.example.wanderwell.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderwell.Helpers.AlertHelper;
import com.example.wanderwell.R;
import com.example.wanderwell.database.Repository;
import com.example.wanderwell.entities.Excursion;
import com.example.wanderwell.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class VacationDetails extends AppCompatActivity {

    private EditText editTitle, editHotel, editEndDate, editStartDate;
    private TextView noExcursionsMessage;
    private CardView  noExcursionsCard;
    private RecyclerView recyclerView;
    private ExcursionAdapter excursionAdapter;
    private Repository repository;

    private int vacationID;
    private String setStartDate, setEndDate;
    private final Calendar myCalendarStart = Calendar.getInstance();
    private final Calendar myCalendarEnd = Calendar.getInstance();
    private final Random rand = new Random();
    private int numAlert = rand.nextInt(99999);


    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        bindViews();
        repository = Repository.getInstance(getApplication());
        getVacationIdFromIntent();

        boolean isEditMode = vacationID != -1;

        if (isEditMode) {
            loadVacationDetails(vacationID);
        } else {
            prefillFromIntent();
        }

//        setupDatePickers();
        setupRecyclerView();
        setupFloatingActionButton();
        setupSaveButton();
    }

    private void getVacationIdFromIntent() {
        Intent intent = getIntent();
        vacationID = (intent != null) ? intent.getIntExtra("vacationId", -1) : -1;
    }

    private void loadVacationDetails(int id) {
        repository.getVacationById(id).observe(this, vacation -> {
            if (vacation != null) {
                editTitle.setText(vacation.getVacationName());
                editHotel.setText(vacation.getHotelName());

                setStartDate = vacation.getStartDate();
                setEndDate = vacation.getEndDate();
                editStartDate.setText(setStartDate);
                editEndDate.setText(setEndDate);
                setupDatePickers();
                loadExcursionsAsync(vacationID);

            }
        });
    }
    private void prefillFromIntent() {
        editTitle.setText(safeExtra("title"));
        editHotel.setText(safeExtra("hotel"));
        setStartDate = safeExtra("startdate");
        setEndDate = safeExtra("enddate");
        editStartDate.setText(setStartDate);
        editEndDate.setText(setEndDate);
    }
    private void setupRecyclerView() {
        excursionAdapter = new ExcursionAdapter(this, new ArrayList<>(), setStartDate, setEndDate);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExcursionDetails.class);
            intent.putExtra("vacationId", vacationID);
            intent.putExtra("vacationStartDate", setStartDate);
            intent.putExtra("vacationEndDate", setEndDate);
            startActivity(intent);
        });
    }
    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.saveVacationButton);
        saveButton.setOnClickListener(v -> saveVacationAsync());
    }
    private void bindViews() {
        editTitle = findViewById(R.id.vacationName);
        editHotel = findViewById(R.id.hotelName);
        editStartDate = findViewById(R.id.vacationStartDate);
        editEndDate = findViewById(R.id.vacationEndDate);
        noExcursionsMessage = findViewById(R.id.noExcursionsMessage);
        noExcursionsCard = findViewById(R.id.noExcursionsCard);
        recyclerView = findViewById(R.id.excursionRecyclerView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadExcursionsAsync();
        updateLabelStart();
        updateLabelEnd();
    }

    private String safeExtra(String key) {
        String value = getIntent().getStringExtra(key);
        return value != null ? value : "";
    }

    private void loadExcursionsAsync() {
        if (vacationID != -1) {
            loadExcursionsAsync(vacationID);
        }
    }
    private void loadExcursionsAsync(int vacationId) {
        repository.getRelatedExcursionsById(vacationId).observe(this, excursions -> {
            if (excursions == null || excursions.isEmpty()) {
                noExcursionsCard.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                noExcursionsCard.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                excursionAdapter.setExcursions(excursions);
            }
        });
    }
    private void setupDatePickers() {
        if (setStartDate != null && !setStartDate.isEmpty()) {
            try {
                myCalendarStart.setTime(sdf.parse(setStartDate));
                myCalendarEnd.setTime(sdf.parse(setEndDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        editStartDate.setOnClickListener(v -> showDateDialog(myCalendarStart, this::updateLabelStart));
        editEndDate.setOnClickListener(v -> showDateDialog(myCalendarEnd, this::updateLabelEnd));
    }

    private void showDateDialog(Calendar calendar, Runnable onSet) {
        new DatePickerDialog(VacationDetails.this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            onSet.run();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabelStart() {
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.vacation_save) {
            saveVacationAsync();
            return true;
        } else if (item.getItemId() == R.id.vacation_delete) {
            deleteVacationAsync();
            return true;
        } else if (item.getItemId() == R.id.alertstart) {
            AlertHelper.setDateAlert(this, editStartDate.getText().toString(), "WOO!! Vacation " + editTitle.getText().toString() + " is starting!", numAlert);
            Toast.makeText(this, "Alert set for the start of the vacation", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.alertend) {
            AlertHelper.setDateAlert(this, editEndDate.getText().toString(), "Vacation " + editTitle.getText().toString() + " is ending :(", numAlert);
            Toast.makeText(this, "Alert set for the end of the vacation", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.alertfull) {
            AlertHelper.setDateAlert(this, editStartDate.getText().toString(), "Vacation " + editTitle.getText().toString() + " is starting", numAlert);
            AlertHelper.setDateAlert(this, editEndDate.getText().toString(), "Vacation " + editTitle.getText().toString() + " is ending", rand.nextInt(99999));
            Toast.makeText(this, "Alert set for the start and end of the vacation", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.share) {
            shareVacation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveVacationAsync() {
        Repository.databaseExecutor.execute(() -> {
            String startDateString = sdf.format(myCalendarStart.getTime());
            String endDateString = sdf.format(myCalendarEnd.getTime());
            try {
                Date startDate = sdf.parse(startDateString);
                Date endDate = sdf.parse(endDateString);

                if (endDate.before(startDate)) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "End date must be AFTER start date", Toast.LENGTH_LONG).show()
                    );
                    return;
                }

                Vacation vacation = new Vacation(
                        vacationID,
                        editTitle.getText().toString(),
                        startDateString,
                        endDateString,
                        editHotel.getText().toString()
                );

                if (vacationID == -1) {
                    List<Vacation> allVacations = repository.getAllVacationsSync();
                    vacationID = allVacations.isEmpty() ? 1 : allVacations.get(allVacations.size() - 1).getVacationId() + 1;
                    vacation.setVacationId(vacationID);
                    repository.insert(vacation);
                } else {
                    repository.update(vacation);
                }

                runOnUiThread(this::finish);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }
    private void deleteVacationAsync() {
        Repository.databaseExecutor.execute(() -> {
            List<Excursion> relatedExcursions = repository.getRelatedExcursionsSync(vacationID);

            if (!relatedExcursions.isEmpty()) {
                runOnUiThread(() -> {
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Delete Not Allowed")
                            .setMessage("You are not permitted to delete a vacation with Excursions. Delete the excursions first then delete the vacation.")
//                            .setPositiveButton("Delete", (dialog, which) -> performVacationDeletion(relatedExcursions))
                            .setNegativeButton("Cancel", null)
                            .show();
                });
            } else {
                performVacationDeletion(new ArrayList<>());
            }
        });
    }

    private void performVacationDeletion(List<Excursion> excursionsToDelete) {
        Repository.databaseExecutor.execute(() -> {
            for (Excursion e : excursionsToDelete) {
                repository.delete(e);
            }
            Vacation vacationToDelete = null;
            for (Vacation v : repository.getAllVacationsSync()) {
                if (v.getVacationId() == vacationID) {
                    vacationToDelete = v;
                    break;
                }
            }
            if (vacationToDelete != null) {
                String name = vacationToDelete.getVacationName();
                repository.delete(vacationToDelete);
                runOnUiThread(() -> {
                    Toast.makeText(this, name + " and related excursions deleted", Toast.LENGTH_LONG).show();
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Vacation not found", Toast.LENGTH_SHORT).show());
            }
        });
    }



    private void shareVacation() {
        Intent sentIntent = new Intent(Intent.ACTION_SEND);
        sentIntent.setType("text/plain");
        sentIntent.putExtra(Intent.EXTRA_SUBJECT, "My Vacation Info: " + editTitle.getText());
        StringBuilder shareData = new StringBuilder();
        shareData.append("Vacation title: ").append(editTitle.getText()).append("\n")
                .append("Hotel name: ").append(editHotel.getText()).append("\n")
                .append("Start Date: ").append(editStartDate.getText()).append("\n")
                .append("End Date: ").append(editEndDate.getText()).append("\n");

        List<Excursion> excursions = excursionAdapter.getExcursions();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        if (excursions.isEmpty()) {
            shareData.append("No excursions added for this vacation.");
        } else {
            shareData.append("*Excursions:*\n");
            for (Excursion e : excursions) {
                String formattedDate = sdf.format(e.getExcursionDate());
                shareData.append("• ")
                        .append(e.getExcursionName())
                        .append(" on ")
                        .append(formattedDate)
                        .append("\n");
            }
        }

        sentIntent.putExtra(Intent.EXTRA_TEXT, shareData.toString());
        startActivity(Intent.createChooser(sentIntent, null));
    }
}
