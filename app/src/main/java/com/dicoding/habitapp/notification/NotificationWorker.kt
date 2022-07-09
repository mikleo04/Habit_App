package com.dicoding.habitapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.data.HabitRepository
import com.dicoding.habitapp.ui.countdown.CountDownActivity
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val habitId = inputData.getInt(HABIT_ID, 0)
    private val habitTitle = inputData.getString(HABIT_TITLE)

    override fun doWork(): Result {
        val prefManager = androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        //TODO 12 : If notification preference on, show notification with pending intent
        if (shouldNotify){
            val habit = HabitRepository.getInstance(applicationContext).getHabitById(habitId)
            CoroutineScope(Dispatchers.Main).launch {
                habit.observeForever {
                    if (it != null){
                        notificationCreated(it)
                    }
                }
            }

        }

        return Result.success()
    }

    private fun pendingIntent(habit: Habit): PendingIntent? {
        val intent = Intent(applicationContext, CountDownActivity::class.java).apply {
            putExtra(HABIT, habit)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun notificationCreated(habit: Habit) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val mBuilder = NotificationCompat.Builder(applicationContext, "myNotification")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(habitTitle)
            .setContentText(applicationContext.getString(R.string.notify_content))
            .setContentIntent(pendingIntent(habit))

        val notification = mBuilder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("myNotification", applicationContext.getString(R.string.notify_channel_name), NotificationManager.IMPORTANCE_DEFAULT)
            mBuilder.setChannelId("myNotification")
            channel.description = applicationContext.getString(R.string.notify_channel_name)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(123, notification)
    }
}
