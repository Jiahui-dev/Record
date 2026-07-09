package com.leben.record.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.leben.base.BaseApplication;
import com.leben.record.model.bean.CategoryEntity;
import com.leben.record.model.bean.ProductEntity;

@Database(entities = {CategoryEntity.class, ProductEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    public abstract CategoryDao categoryDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance() {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            BaseApplication.getAppContext(),
                            AppDatabase.class,
                            "record.db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}
