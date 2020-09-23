package com.example.studytimetwo.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.studytimetwo.R
import com.example.studytimetwo.data.StudyDatabase
import com.example.studytimetwo.data.StudySession
import com.example.studytimetwo.databinding.ActivityTimerBinding
import com.example.studytimetwo.ui.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_timer.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private  val viewModel: MainActivityViewModel by viewModels()

    private var START_MILLI_SECONDS = 0L
    private var countdown_timer: CountDownTimer? = null
    var isRunning: Boolean = false;
    private var time_in_milli_seconds = 0L
    private var time_in_hours = 0L
    private lateinit var studySession: StudySession

    private val currentDayOfMonth = LocalDateTime.now().dayOfMonth
    private val currentMonth = LocalDateTime.now().monthValue
    private val currentWeekDay = LocalDateTime.now().dayOfWeek
    private val currentDate = LocalDateTime.now()
    private val currentYear = LocalDateTime.now().year
    private val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timer)
        binding.lifecycleOwner = this

        binding.startButton.isEnabled = false


        binding.startButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                val time  = etTimeInput.text.toString()
                time_in_hours = time.toLong()
                time_in_milli_seconds = TimeUnit.HOURS.toMillis(time_in_hours)

                startTimer(time_in_milli_seconds)
            }
        }

        binding.etTimeInput.setOnClickListener {
            binding.startButton.isEnabled = true
        }




        //Adding a study session needs to insert into database
        binding.addStudySessionChip.setOnClickListener {
            if(binding.etTimeInput.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter a study duration", Toast.LENGTH_SHORT).show()
            }else{
                binding.startButton.text = "start"
                countdown_timer?.cancel()
                val minutesStudied = TimeUnit.MILLISECONDS.toMinutes(time_in_milli_seconds)
                val hoursStudied = (minutesStudied/60.0).toFloat()

                studySession = StudySession(
                    hours = hoursStudied,
                    minutes = minutesStudied,
                    date = formattedDate.toString(),
                    weekDay = currentWeekDay.toString(),
                    month = currentMonth,
                    dayOfMonth = currentDayOfMonth,
                    year = currentYear,
                )

                viewModel.upsertStudySession(studySession)
                finish()
            }
        }
        binding.btnReset.setOnClickListener {
            resetTimer()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("savedMiliseconds", time_in_milli_seconds)
    }

    private fun pauseTimer() {
        startButton.text = "Start"
        countdown_timer?.cancel()
        isRunning = false
    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                saveSessionDialog()
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer?.start()
        isRunning = true
        startButton.text = "Pause"
    }

    private fun resetTimer() {
        binding.startButton.text = "start"
        countdown_timer?.cancel()
        time_in_milli_seconds = START_MILLI_SECONDS
        updateTextUI()
    }

    private fun updateTextUI() {

        val time = String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(time_in_milli_seconds),
            TimeUnit.MILLISECONDS.toMinutes(time_in_milli_seconds) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_in_milli_seconds)), // The change is in this line
            TimeUnit.MILLISECONDS.toSeconds(time_in_milli_seconds) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time_in_milli_seconds)));

        tvTimerCountDown.text = "$time"
    }


    private fun saveSessionDialog(){

        studySession = StudySession(
            date = formattedDate.toString(),
            hours = time_in_hours.toFloat(),
            minutes = 0,
            weekDay = currentWeekDay.toString(),
            dayOfMonth = currentDayOfMonth,
            month = currentMonth,
            year = currentYear)

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Save this session?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.upsertStudySession(studySession)
                finish()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Save study session")
        alert.show()
    }
}