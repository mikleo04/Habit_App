package com.dicoding.habitapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2 : Define data access object (DAO)
@Dao
interface HabitDao {
    @RawQuery(observedEntities = [Habit::class])
    fun getHabits(query: SupportSQLiteQuery): DataSource.Factory<Int, Habit>

    @Query("SELECT * FROM habits WHERE id=:habitId")
    fun getHabitById(habitId: Int): LiveData<Habit>

    @Insert(entity = Habit::class)
    fun insertHabit(habit: Habit): Long

    @Insert(entity = Habit::class)
    fun insertAll(vararg habits: Habit)

    @Delete(entity = Habit::class)
    fun deleteHabit(habits: Habit)

    @Query("SELECT * FROM habits WHERE priorityLevel=:level")
    fun getRandomHabitByPriorityLevel(level: String): LiveData<Habit>
}
