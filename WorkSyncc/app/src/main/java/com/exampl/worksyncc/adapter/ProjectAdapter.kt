package com.exampl.worksyncc.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exampl.worksyncc.databinding.ItemProjectBinding
import com.exampl.worksyncc.model.Project

class ProjectAdapter(
    private var projects: List<Project>,
    private val onItemClick: ((Project) -> Unit)? = null
) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(val binding: ItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        with(holder.binding) {
            txtProjectName.text = project.name
            txtClientName.text = project.client
            pbProjectProgress.progress = project.progress
            txtMembersCount.text = "👥 ${project.membersCount} Members"
            txtDeadline.text = project.deadline
            
            try {
                txtDeadline.setTextColor(Color.parseColor(project.deadlineColorHex))
            } catch (e: Exception) {
                // fallback
            }

            root.setOnClickListener {
                onItemClick?.invoke(project)
            }
        }
    }

    override fun getItemCount(): Int = projects.size

    fun updateData(newProjects: List<Project>) {
        projects = newProjects
        notifyDataSetChanged()
    }
}
