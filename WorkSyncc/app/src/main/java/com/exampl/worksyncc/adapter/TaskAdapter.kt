package com.exampl.worksyncc.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exampl.worksyncc.databinding.ItemTaskBinding
import com.exampl.worksyncc.model.Task

class TaskAdapter(
    private var tasks: List<Task>,
    private val onItemClick: ((Task) -> Unit)? = null
) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        with(holder.binding) {
            txtTaskTitle.text = task.title
            txtTaskProject.text = task.project
            txtTaskPriority.text = task.priority
            txtTaskStatus.text = task.status
            txtAssignedTo.text = task.assignedTo
            
            val statusBg = when(task.status) {
                "In Progress" -> com.exampl.worksyncc.R.drawable.bg_status_in_progress
                "Done" -> com.exampl.worksyncc.R.drawable.bg_status_done
                else -> com.exampl.worksyncc.R.drawable.bg_status_todo
            }
            txtTaskStatus.setBackgroundResource(statusBg)

            try {
                val color = Color.parseColor(task.priorityColorHex)
                viewPriorityStrip.setBackgroundColor(color)
                txtTaskPriority.setTextColor(color)
            } catch (e: Exception) {
                // fallback
            }

            root.setOnClickListener {
                onItemClick?.invoke(task)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateData(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
