package com.example.studytimetwo.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.studytimetwo.R
import com.example.studytimetwo.data.StudyDatabase
import com.example.studytimetwo.databinding.ActivityMainBinding
import com.example.studytimetwo.ui.fragments.MonthViewFragment
import com.example.studytimetwo.ui.fragments.WeekViewFragment
import com.example.studytimetwo.ui.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewmodel: MainActivityViewModel by viewModels()
    private var displayWeekFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this





//        viewmodel.insertAStudySession()

        if (savedInstanceState != null) {
            if(!savedInstanceState.getBoolean("display")){
                supportFragmentManager.commit {
                    replace<MonthViewFragment>(R.id.fragment_container, null)
                    displayWeekFragment = true
                }
            }
        }else{
            supportFragmentManager.commit {
                replace<WeekViewFragment>(R.id.fragment_container, null)
                displayWeekFragment = true
            }
        }

        binding.weekChip.setOnClickListener {
            supportFragmentManager.commit {
                replace<WeekViewFragment>(R.id.fragment_container, null)
                addToBackStack(null)
                displayWeekFragment = true
            }
        }

        binding.monthChip.setOnClickListener {
            supportFragmentManager.commit {
                replace<MonthViewFragment>(R.id.fragment_container, null)
                addToBackStack(null)
                displayWeekFragment = false
            }
        }
        binding.addSessionChip.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        binding.sessionsChip.setOnClickListener {
            val intent = Intent(this, AllSessionsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("display", displayWeekFragment)
    }
}