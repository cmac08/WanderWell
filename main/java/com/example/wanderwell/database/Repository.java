package com.example.wanderwell.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.wanderwell.dao.ExcursionDAO;
import com.example.wanderwell.dao.VacationDAO;
import com.example.wanderwell.entities.Excursion;
import com.example.wanderwell.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private static Repository instance;
    private final VacationDAO mVacationDAO;
    private final ExcursionDAO mExcursionDAO;
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public Repository(Application application) {
        vacationDatabaseBuilder db = vacationDatabaseBuilder.getDatabase(application);
        mVacationDAO = db.vacationDAO();
        mExcursionDAO = db.excursionDAO();
        //databaseExecutor = Executors.newFixedThreadPool(4);
    }
    public static synchronized Repository getInstance(Application application) {
        if (instance == null) {
            instance = new Repository(application);
        }
        return instance;
    }


    // Vacation methods
    public List<Vacation> getAllVacationsSync() {
        return mVacationDAO.getAllVacationsSync();
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.insert(vacation));
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.update(vacation));
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.delete(vacation));
    }

    public LiveData<Vacation> getVacationById(int vacationId) {
        return mVacationDAO.getVacationById(vacationId);
    }

    public List<Vacation> getVacationsByName(String name) {
        return mVacationDAO.getVacationsByName(name);
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return mVacationDAO.getAllVacations();
    }

    // Excursion methods
    public List<Excursion> getAllExcursionsSync() {
        return mExcursionDAO.getAllExcursionsSync();
    }

    public LiveData<List<Excursion>> getRelatedExcursionsById(int vacationId) {
        return mExcursionDAO.getRelatedExcursions(vacationId);
    }

    public List<Excursion> getRelatedExcursionsSync(int vacationId) {
        return mExcursionDAO.getRelatedExcursionsSync(vacationId);
    }

    public Excursion getExcursionById(int id) {
        return mExcursionDAO.getExcursionById(id);
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.insert(excursion));
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.update(excursion));
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.delete(excursion));
    }

    public LiveData<List<Vacation>> searchVacations(String query) {
        return mVacationDAO.searchVacations(query);
    }

    public LiveData<List<Excursion>> searchExcursions(String query) {
        return mExcursionDAO.searchExcursions(query);
    }
}
