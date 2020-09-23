package com.example.studytimetwo.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.studytimetwo.R
import com.example.studytimetwo.data.StudyDatabase
import com.example.studytimetwo.databinding.ActivityMonthDetailBinding
import com.example.studytimetwo.ui.viewmodels.MonthDetailViewModel
import com.github.mikephil.charting.data.BarData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthDetailActivity : AppCompatActivity() {
    private val viewModel: MonthDetailViewModel by viewModels()
    private lateinit var binding: ActivityMonthDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_month_detail)

        binding.lifecycleOwner = this


        viewModel.monthBarData.observe(this, Observer {
            it?.let {
                setBarChart(it)
            }
        })
    }

    private fun setBarChart(barData: BarData) {
        val xaxis = binding.activityMonthBarChart.xAxis //sets the spacing between the x labels
        xaxis.spaceBetweenLabels = 0

        binding.activityMonthBarChart.data = barData // set the data and list of lables into chart
        binding.activityMonthBarChart.setDescription(viewModel.month)

        binding.activityMonthBarChart.animateY(2000)
    }
}