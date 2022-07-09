package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import java.util.concurrent.TimeUnit

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
        val initialTime = habit.minutesFocus
        viewModel.setInitialTime(initialTime)
        viewModel.currentTimeString.observe(this){
            findViewById<TextView>(R.id.tv_count_down).text = it
        }
        viewModel.eventCountDownFinish.observe(this){
            updateButtonState(!it)
        }

        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            configureWManager(false, habit)
            viewModel.startTimer()
            configureWManager(true, habit)
            notifyNotification()
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.resetTimer()
            configureWManager(false, habit)
        }
    }

    private fun getPreferenceNotification()
            = androidx.preference.PreferenceManager
        .getDefaultSharedPreferences(applicationContext)
        .getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

    private fun notifyNotification(){
        val notificationStatus = getPreferenceNotification()
        if (!notificationStatus)
            Toast.makeText(this, "Turn on notification in Setting for Notification", Toast.LENGTH_LONG).show()
    }
    private fun configureWManager(setEnable: Boolean, habit: Habit) {
        val wManager = WorkManager.getInstance(this)
        val delay = (habit.minutesFocus * 60 * 1000);
        val workerTag = "notification_worker_tag_1976829450135"
        val data = Data.Builder()
            .putInt(HABIT_ID, habit.id)
            .putString(HABIT_TITLE, habit.title)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(data)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(workerTag)
            .build()

        if (!getPreferenceNotification()){
            return
        }

        if (setEnable){
            wManager.enqueue(oneTimeWorkRequest)
            wManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this) { workInfo ->
                val status = workInfo.state.name
                Log.d("worker", "onCreate: Now Status ${status}")
                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    Log.d("worker", "onCreate: Worker Is Enqueued")
                }
            }
        }else{
            wManager.cancelAllWorkByTag(workerTag)
            Log.d("TAG", "onCreate: Worker Is Cancel")
        }

    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}