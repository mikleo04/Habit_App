package com.dicoding.habitapp.ui.list

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.add.AddHabitActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//TODO 16 : Write UI test to validate when user tap Add Habit (+), the AddHabitActivity displayed
@RunWith(AndroidJUnit4ClassRunner::class)
class HabitActivityTest {
    @Before
    fun before(){
        ActivityScenario.launch(HabitListActivity::class.java)
    }
    @Test
    fun testAddHabitActivityWhenFabBtnTapped() {
        Intents.init()
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(AddHabitActivity::class.java.name))
    }
}