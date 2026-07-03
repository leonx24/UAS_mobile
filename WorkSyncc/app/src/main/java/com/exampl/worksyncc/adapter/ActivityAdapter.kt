package com.exampl.worksyncc.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exampl.worksyncc.databinding.ItemActivityBinding
import com.exampl.worksyncc.model.Activity

class ActivityAdapter(private var activities: List<Activity>) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(val binding: ItemActivityBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        with(holder.binding) {
            txtActivityDesc.text = activity.description
            txtActivityTime.text = activity.timestamp
            
            if (activity.description.contains("submit", true) || activity.description.contains("approved", true)) {
                txtActivityIcon.text = "✔"
                txtActivityIcon.setTextColor(Color.parseColor("#10B981")) // green
            } else {
                txtActivityIcon.text = "•"
                txtActivityIcon.setTextColor(Color.parseColor("#2563EB")) // blue
            }
        }
    }

    override fun getItemCount(): Int = activities.size

    fun updateData(newActivities: List<Activity>) {
        activities = newActivities
        notifyDataSetChanged()
    }
}
