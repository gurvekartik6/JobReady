package com.jobread.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.jobread.app.database.converters.Converters;
import com.jobread.app.database.dao.JobDao;
import com.jobread.app.database.entities.JobEntity;

@Database(
    entities = {JobEntity.class},
    version = 1,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "jobread_database";
    private static volatile AppDatabase sInstance;

    public abstract JobDao jobDao();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DB_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return sInstance;
    }

    public static AppDatabase getInMemoryInstance(Context context) {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    public static void destroyInstance() {
        sInstance = null;
    }
}
