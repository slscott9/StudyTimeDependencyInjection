package com.example.studytimetwo.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.studytimetwo.R
import com.example.studytimetwo.ui.adapters.MonthSessionsAdapter
import com.example.studytimetwo.ui.adapters.YearSessionsAdapter
import com.example.studytimetwo.ui.viewmodels.AllSessionsViewModel
import com.example.studytimetwo.ui.viewmodels.MonthDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class AllSessionsActivity : AppCompatActivity() {
    private val viewModel: AllSessionsViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_sessions)



        val adapter = YearSessionsAdapter() {

            Toast.makeText(this, "The item clicked is $it", Toast.LENGTH_LONG).show()
            viewModel.getMonthsWithSelectedYear(it)
        }//no list in constructor so we can manually set the list when data changes


        val monthAdapter = MonthSessionsAdapter{
            val intent = Intent(this, MonthDetailActivity::class.java)
            intent.putExtra(MonthDetailViewModel.MONTH_SELECTED, it)
            startActivity(intent)
        }

        viewModel.monthsFromSelectedYear.observe(this, Observer {
            it?.let {
                monthAdapter.setData(it)
            }
        })


        viewModel.yearList.observe(this, Observer {
            it?.let {
                adapter.setData(it) //When the data changes (we get results from database) set the adapter's empty list to viiew model's list of years from database
            }
        })

        val monthRecyclerView = findViewById<RecyclerView>(R.id.month_recycler_view)
        monthRecyclerView.adapter = monthAdapter

        val recyclerView = findViewById<RecyclerView>(R.id.yearsListRecyclerView)
        recyclerView.adapter = adapter




    }

}