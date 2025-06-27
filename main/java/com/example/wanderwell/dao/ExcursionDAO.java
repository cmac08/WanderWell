package com.example.wanderwell.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wanderwell.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM Excursions ORDER BY excursionId ASC")
    LiveData<List<Excursion>> getAllExcursions();

    @Query("SELECT * FROM Excursions WHERE vacationId = :vacation ORDER BY excursionId ASC")
    LiveData<List<Excursion>> getRelatedExcursions(int vacation);
    @Query("SELECT * FROM Excursions WHERE excursionId = :excursionId")
    Excursion getExcursionById(int excursionId);

    @Query("SELECT * FROM Excursions WHERE excursionName LIKE '%' || :query || '%' OR excursionDate LIKE '%' || :query || '%'")
    LiveData<List<Excursion>> searchExcursions(String query);
    @Query("SELECT * FROM Excursions ORDER BY excursionId ASC")
    List<Excursion> getAllExcursionsSync();

    @Query("SELECT * FROM Excursions WHERE vacationId = :vacation ORDER BY excursionId ASC")
    List<Excursion> getRelatedExcursionsSync(int vacation);
}
