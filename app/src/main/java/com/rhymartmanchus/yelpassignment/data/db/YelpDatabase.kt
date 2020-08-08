package com.rhymartmanchus.yelpassignment.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryAssocDB
import com.rhymartmanchus.yelpassignment.data.db.models.CategoryDB

@Database(
    entities = [
        CategoryDB::class,
        CategoryAssocDB::class
    ],
    version = 1
)
abstract class YelpDatabase : RoomDatabase() {

    abstract fun categoriesDao(): CategoriesDao

    companion object {

        internal var INSTANCE: YelpDatabase? = null

        fun getInstance(appContext: Context): YelpDatabase {
            if(INSTANCE == null) {
                synchronized(YelpDatabase::class) {
                    INSTANCE = Room.databaseBuilder(appContext, YelpDatabase::class.java, "yelp-database.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}