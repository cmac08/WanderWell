package com.example.wanderwell.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wanderwell.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations")
    LiveData<List<Vacation>> getAllVacations();
    @Query("SELECT * FROM Vacations WHERE vacationId = :vacationId")
    LiveData<Vacation> getVacationById(int vacationId);
    @Query("SELECT * FROM Vacations WHERE vacationName = :name")
    List<Vacation> getVacationsByName(String name);

    @Query("SELECT * FROM vacations WHERE vacationName LIKE '%' || :query || '%' OR startDate LIKE '%' || :query || '%' OR endDate LIKE '%' || :query || '%'")
    LiveData<List<Vacation>> searchVacations(String query);

    @Query("SELECT * FROM Vacations")
    List<Vacation> getAllVacationsSync();
}
