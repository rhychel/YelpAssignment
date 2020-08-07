package com.rhymartmanchus.yelpassignment.data

import android.content.Context
import androidx.room.Room
import com.rhymartmanchus.yelpassignment.data.db.YelpDatabase

abstract class TestYelpDatabase : YelpDatabase() {

    companion object {
        fun getInstance(appContext: Context): YelpDatabase {
            if(INSTANCE == null) {
                synchronized(YelpDatabase::class.java) {
                    INSTANCE = Room.inMemoryDatabaseBuilder(appContext, YelpDatabase::class.java)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE?.clearAllTables()
            INSTANCE = null
        }
    }

}