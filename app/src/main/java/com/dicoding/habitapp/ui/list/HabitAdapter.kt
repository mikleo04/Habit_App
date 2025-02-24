package com.dicoding.habitapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit

class HabitAdapter(
    private val onClick: (Habit) -> Unit
) : PagedListAdapter<Habit, HabitAdapter.HabitViewHolder>(DIFF_CALLBACK) {

    //TODO 8 : Create and initialize ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.habit_item, parent, false) as View)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        //TODO 9 : Get data and bind them to ViewHolder
        holder.bind(getItem(position) as Habit)
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.item_tv_title)
        val ivPriority: ImageView = itemView.findViewById(R.id.item_priority_level)
        private val tvStartTime: TextView = itemView.findViewById(R.id.item_tv_start_time)
        private val tvMinutes: TextView = itemView.findViewById(R.id.item_tv_minutes)

        lateinit var getHabit: Habit
        fun bind(habit: Habit) {
            getHabit = habit
            tvTitle.text = habit.title
            tvStartTime.text = habit.startTime
            tvMinutes.text = habit.minutesFocus.toString()
            itemView.setOnClickListener {
                onClick(habit)
            }

            val getIcon = when(habit.priorityLevel){
                itemView.context.getString(R.string.high) -> { AppCompatResources.getDrawable(itemView.context, R.drawable.ic_priority_high)}
                itemView.context.getString(R.string.medium) -> { AppCompatResources.getDrawable(itemView.context, R.drawable.ic_priority_medium)}
                else -> { AppCompatResources.getDrawable(itemView.context, R.drawable.ic_priority_low)}
            }
            ivPriority.setImageDrawable( getIcon )
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem == newItem
            }
        }

    }

}