package com.arheisel.budgeteer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.arheisel.budgeteer.database.AppDatabase
import com.arheisel.budgeteer.database.Expense
import com.arheisel.budgeteer.database.ExpenseDao
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var expenseDao: ExpenseDao

    private lateinit var budgetView: TextView
    private lateinit var descInput: EditText
    private lateinit var amountInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.build(applicationContext)
        expenseDao = db.expenseDao()

        budgetView = findViewById(R.id.budget)
        descInput = findViewById(R.id.descInput)
        amountInput = findViewById(R.id.amountInput)

        val addExpenseBtn = findViewById<Button>(R.id.addExpense)
        addExpenseBtn.setOnClickListener{ addExpense() }

        updateBudget()
    }

    private fun updateBudget() {
        lifecycleScope.launch {
            val currDate = ZonedDateTime.now()
            val expenses = expenseDao.getNewerThan(currDate.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS))

            var sum = 0.0
            for (i in expenses) sum += i.amount

            val budget = (20000 * currDate.dayOfMonth) - sum
            budgetView.text = String.format(Locale.getDefault(),"$ %,.2f", budget)
        }
    }

    private fun addExpense() {
        lifecycleScope.launch {
            try {
                val desc = descInput.text.toString()
                val amount = amountInput.text.toString().toDouble()
                require(amount > 0)

                val expense = Expense(
                    date = ZonedDateTime.now(),
                    desc = desc,
                    amount = amount
                )

                expenseDao.insertAll(expense)
                updateBudget()
                descInput.text = null
                amountInput.text = null
            }
            catch (e: Exception) {
                showDialog(e.toString())
            }
        }
    }

    private fun showDialog(text: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(text)
        builder.setPositiveButton("OK") { _, _ -> }
        val dialog = builder.create()
        dialog.show()
    }
}