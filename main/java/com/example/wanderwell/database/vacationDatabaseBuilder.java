package com.example.wanderwell.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.wanderwell.dao.ExcursionDAO;
import com.example.wanderwell.dao.VacationDAO;
import com.example.wanderwell.entities.Excursion;
import com.example.wanderwell.entities.Vacation;

@Database(entities = {Excursion.class, Vacation.class}, version = 4, exportSchema = false)
//when updating the database, update the version each time
public abstract class vacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile vacationDatabaseBuilder INSTANCE;

    static vacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null){
           synchronized (vacationDatabaseBuilder.class) {
               if (INSTANCE == null){
                   INSTANCE = Room.databaseBuilder(context.getApplicationContext(), vacationDatabaseBuilder.class, "MyVacationDatabase.db")
                           .fallbackToDestructiveMigration()
                           .build();
               }
           }
        }
        return INSTANCE;
    }
}
