package com.dicoding.habitapp.ui.random

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit

class RandomHabitAdapter(
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<RandomHabitAdapter.PagerViewHolder>() {

    private val habitMap = LinkedHashMap<PageType, Habit>()

    fun submitData(key: PageType, habit: Habit) {
        habitMap[key] = habit
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.pager_item, parent, false))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val key = getIndexKey(position) ?: return
        val pageData = habitMap[key] ?: return
        holder.bind(key, pageData)
    }

    override fun getItemCount() = habitMap.size

    private fun getIndexKey(position: Int) = habitMap.keys.toTypedArray().getOrNull(position)

    enum class PageType {
        HIGH, MEDIUM, LOW
    }

    inner class PagerViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //TODO 14 : Create view and bind data to item view
        fun bind(pageType: PageType, pageData: Habit) {
            pageData.apply {
                (itemView.findViewById(R.id.pager_tv_title) as TextView).text = title
                (itemView.findViewById(R.id.pager_tv_minutes) as TextView).text = minutesFocus.toString()
                (itemView.findViewById(R.id.pager_tv_start_time) as TextView).text = startTime
                
                val icn = if(pageType == PageType.HIGH)
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_priority_high)
                else if(pageType == PageType.MEDIUM)
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_priority_medium)
                else
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_priority_low)
                (itemView.findViewById(R.id.item_priority_level) as ImageView).setImageDrawable( icn )
                (itemView.findViewById(R.id.btn_open_count_down) as Button).setOnClickListener { onClick(this) }
            }
        }
    }
}
