package com.melitopolcherry.timester.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.melitopolcherry.timester.data.database.converters.LocalDateTypeConverter
import com.melitopolcherry.timester.data.database.dao.EventsDao
import com.melitopolcherry.timester.data.database.entity.Event

@TypeConverters(
    LocalDateTypeConverter::class
)
@Database(entities = [Event::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventsDao(): EventsDao

    companion object {
        private const val DATABASE_NAME = "Timester.db"

        private var dbInstanceNullable: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            return dbInstanceNullable ?: synchronized(LOCK) {
                dbInstanceNullable ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { dbInstanceNullable = it }
            }
        }
    }
}
