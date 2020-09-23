package com.example.studytimetwo.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.studytimetwo.R
import com.example.studytimetwo.databinding.FragmentMonthViewBinding
import com.example.studytimetwo.ui.viewmodels.MainActivityViewModel
import com.github.mikephil.charting.data.BarData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthViewFragment : Fragment() {


    private lateinit var binding: FragmentMonthViewBinding
    private val viewModel: MainActivityViewModel by activityViewModels() //accesses parent activity's view model. can only be accessed after the fragment is attached to activity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_month_view, container, false )


//This is how we get a view to wait until data is ready. Make the barData live data. When there is a change (we get the data from the database) we then set the view data
        viewModel.monthBarData.observe(viewLifecycleOwner, Observer {
            it?.let {
                setBarChart(it)
            }
        })

        return binding.root
    }


    private fun setBarChart(barData: BarData) {
        val xaxis = binding.monthBarChart.xAxis //sets the spacing between the x labels
        xaxis.spaceBetweenLabels = 0

        binding.monthBarChart.fitScreen()
        binding.monthBarChart.data = barData // set the data and list of lables into chart
        binding.monthBarChart.setDescription(viewModel.month)

        binding.monthBarChart.animateY(1000)
    }




}