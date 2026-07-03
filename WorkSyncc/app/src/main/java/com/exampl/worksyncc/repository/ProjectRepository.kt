package com.exampl.worksyncc.repository

import com.exampl.worksyncc.model.Project
import kotlinx.coroutines.delay

class ProjectRepository {

    suspend fun getProjects(): List<Project> {
        delay(800) // Simulate network delay
        return listOf(
            Project("Website Company Profile", "PT Maju Bersama", 80, 5, "30 Jul", "#EF4444"),
            Project("Mobile Banking", "Bank Indonesia", 55, 8, "15 Aug", "#F59E0B"),
            Project("E-Commerce App", "Global Store", 30, 12, "10 Sep", "#2563EB"),
            Project("Internal CRM", "Internal", 100, 4, "Finished", "#10B981"),
            Project("Inventory System", "Logistic Co", 15, 6, "05 Oct", "#F59E0B")
        )
    }

    suspend fun getProjectDetail(projectName: String): Project? {
        // In a real app, this would use an ID. Using name as mock ID for now.
        return getProjects().find { it.name == projectName }
    }

    suspend fun createProject(project: Project): Boolean {
        delay(1000)
        return true
    }

    suspend fun updateProject(project: Project): Boolean {
        delay(1000)
        return true
    }

    suspend fun deleteProject(projectName: String): Boolean {
        delay(1000)
        return true
    }
}
