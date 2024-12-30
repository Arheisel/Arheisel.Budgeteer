package com.arheisel.budgeteer.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.ZonedDateTime

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: ZonedDateTime,
    val desc: String? = null,
    val amount: Double
)

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses")
    suspend fun getAll(): List<Expense>

    @Query("SELECT * FROM expenses WHERE date >= :date")
    suspend fun getNewerThan(date: ZonedDateTime) : List<Expense>

    @Insert
    suspend fun insertAll(vararg expenses: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}