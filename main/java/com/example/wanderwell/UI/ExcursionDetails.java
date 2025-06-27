package com.example.wanderwell.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderwell.Activities.MyReceiver;
import com.example.wanderwell.R;
import com.example.wanderwell.database.Repository;
import com.example.wanderwell.entities.Excursion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    private EditText editExcursionTitle, editExcursionDate;
    private Repository repository;
    private int excursionId, vacationId;
    private String vacationStartDateStr, vacationEndDateStr;

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        TextView dateRangeLabel = findViewById(R.id.textViewVacationDateRange);
        repository = new Repository(getApplication());

        editExcursionTitle = findViewById(R.id.excursionName);
        editExcursionDate = findViewById(R.id.excursionDate);
        Button saveButton = findViewById(R.id.saveExcursionButton);
        Button deleteButton = findViewById(R.id.buttonDeleteExcursion);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        excursionId = getIntent().getIntExtra("excursionId", -1);
        vacationId = getIntent().getIntExtra("vacationId", -1);
        vacationStartDateStr = getIntent().getStringExtra("vacationStartDate");
        vacationEndDateStr = getIntent().getStringExtra("vacationEndDate");
        dateRangeLabel.setText("Vacation Dates: " + vacationStartDateStr + " – " + vacationEndDateStr);


        setupDatePicker();
        loadExcursionDetailsAsync();
        setupRecyclerView();

        saveButton.setOnClickListener(v -> saveOrUpdateExcursionAsync());
        deleteButton.setOnClickListener(v -> deleteExcursionAsync());
    }

    private void setupDatePicker() {
        editExcursionDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = (month + 1) + "/" + dayOfMonth + "/" + year;
                editExcursionDate.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadExcursionDetailsAsync() {
        if (excursionId == -1) return;

        Repository.databaseExecutor.execute(() -> {
            Excursion excursion = repository.getExcursionById(excursionId);
            if (excursion != null) {
                runOnUiThread(() -> {
                    editExcursionTitle.setText(excursion.getExcursionName());
                    editExcursionDate.setText(sdf.format(excursion.getExcursionDate()));
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Excursion not found.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewExcursionDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ExcursionAdapter excursionAdapter = new ExcursionAdapter(this, new ArrayList<>(), vacationStartDateStr, vacationEndDateStr);
        recyclerView.setAdapter(excursionAdapter);

        Repository.databaseExecutor.execute(() -> {
            List<Excursion> excursions = repository.getRelatedExcursionsSync(vacationId);
            runOnUiThread(() -> excursionAdapter.setExcursions(excursions));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.saveExcursion) {
            saveOrUpdateExcursionAsync();
            return true;
        } else if (item.getItemId() == R.id.deleteExcursion) {
            deleteExcursionAsync();
            return true;
        } else if (item.getItemId() == R.id.setNotification) {
            setNotificationAsync();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveOrUpdateExcursionAsync() {
        Repository.databaseExecutor.execute(() -> {
            try {
                String title = editExcursionTitle.getText() != null ? editExcursionTitle.getText().toString().trim() : "";
                String dateInput = editExcursionDate.getText() != null ? editExcursionDate.getText().toString().trim() : "";

                if (title.isEmpty() || dateInput.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show());
                    return;
                }

                Date parsedExcursionDate = sdf.parse(dateInput);
                Date parsedVacationStartDate = sdf.parse(vacationStartDateStr);
                Date parsedVacationEndDate = sdf.parse(vacationEndDateStr);

                if (parsedExcursionDate == null || parsedVacationStartDate == null || parsedVacationEndDate == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Invalid date(s).", Toast.LENGTH_SHORT).show());
                    return;
                }

                boolean isBeforeStart = parsedExcursionDate.before(parsedVacationStartDate);
                boolean isAfterEnd = parsedExcursionDate.after(parsedVacationEndDate);

                if (isBeforeStart || isAfterEnd) {
                    runOnUiThread(() -> Toast.makeText(this, "Excursion date must be within vacation dates.", Toast.LENGTH_SHORT).show());
                    return;
                }

                Excursion newExcursion = new Excursion(
                        excursionId == -1 ? 0 : excursionId,
                        title,
                        parsedExcursionDate,
                        vacationId
                );

                if (excursionId == -1) {
                    repository.insert(newExcursion);
                    runOnUiThread(() -> Toast.makeText(this, "Excursion added.", Toast.LENGTH_SHORT).show());
                } else {
                    repository.update(newExcursion);
                    runOnUiThread(() -> Toast.makeText(this, "Excursion updated.", Toast.LENGTH_SHORT).show());
                }

                runOnUiThread(this::finish);

            } catch (ParseException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Invalid date format.", Toast.LENGTH_SHORT).show());
            }
        });
    }


    private void deleteExcursionAsync() {
        if (excursionId == -1) {
            Toast.makeText(this, "No excursion to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Excursion")
                .setMessage("Are you sure you want to delete this excursion?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Repository.databaseExecutor.execute(() -> {
                        Excursion excursion = repository.getExcursionById(excursionId);
                        if (excursion != null) {
                            repository.delete(excursion);
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Excursion deleted.", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Excursion not found.", Toast.LENGTH_SHORT).show());
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void setNotificationAsync() {
        if (excursionId == -1) {
            Toast.makeText(this, "Cannot set alarm for new excursion.", Toast.LENGTH_SHORT).show();
            return;
        }

        Repository.databaseExecutor.execute(() -> {
            Excursion excursion = repository.getExcursionById(excursionId);
            if (excursion != null) {
                runOnUiThread(() -> setExcursionAlarm(excursion));
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Excursion not found.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setExcursionAlarm(Excursion excursion) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Toast.makeText(this, "Alarm service unavailable.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("excursionTitle", excursion.getExcursionName());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, excursion.getExcursionId(), intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(excursion.getExcursionDate());

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm set for excursion: " + excursion.getExcursionName(), Toast.LENGTH_SHORT).show();
    }
}
