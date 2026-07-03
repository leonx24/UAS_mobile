package com.exampl.worksyncc.repository

import com.exampl.worksyncc.model.Task
import kotlinx.coroutines.delay

class TaskRepository {

    suspend fun getTasks(): List<Task> {
        delay(600)
        return listOf(
            Task("Login Screen UI", "Website Company Profile", "High Priority", "#EF4444"),
            Task("Dashboard API", "Mobile Banking", "Medium Priority", "#F59E0B"),
            Task("Unit Testing", "E-Commerce App", "Low Priority", "#2563EB"),
            Task("Bug Fixing", "Internal CRM", "High Priority", "#EF4444")
        )
    }

    suspend fun getTasksByProject(projectName: String): List<Task> {
        return getTasks().filter { it.project == projectName }
    }

    suspend fun createTask(task: Task): Boolean {
        delay(800)
        return true
    }

    suspend fun updateTaskStatus(taskName: String, status: String): Boolean {
        delay(500)
        return true
    }
}
