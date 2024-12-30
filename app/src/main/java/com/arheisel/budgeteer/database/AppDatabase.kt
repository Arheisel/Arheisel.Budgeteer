package com.arheisel.budgeteer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Expense::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        fun build(context: Context) : AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, "app-db"
                ).build()
        }
    }
}