package com.exampl.worksyncc.repository

import com.exampl.worksyncc.api.DashboardApi
import com.exampl.worksyncc.model.Activity
import com.exampl.worksyncc.model.Project
import com.exampl.worksyncc.model.Task

class DashboardRepository(private val api: DashboardApi? = null) {

    companion object {
        private const val PREFS_NAME = "worksync_prefs"
        private const val KEY_PROJECTS = "mock_projects"
        private const val KEY_TASKS = "mock_tasks"
        private const val KEY_ACTIVITIES = "mock_activities"

        private val prefs by lazy {
            com.exampl.worksyncc.WorkSyncApp.getContext().getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
        }
        
        private val gson = com.google.gson.Gson()

        private var mockProjects: MutableList<Project> = loadProjects() ?: mutableListOf(
            Project("Website Company Profile", "PT Maju Bersama", 80, 5, "30 Jul", "#EF4444"),
            Project("Mobile Banking", "Bank Indonesia", 55, 8, "15 Aug", "#F59E0B")
        )

        private var mockTasks: MutableList<Task> = loadTasks() ?: mutableListOf(
            Task("Login Screen UI", "Website Company Profile", "High Priority", "#EF4444", "In Progress", "Leon"),
            Task("Dashboard API", "Mobile Banking", "Medium Priority", "#F59E0B", "To Do", "Andi"),
            Task("Unit Testing", "Mobile Banking", "Low Priority", "#10B981", "Done", "Budi")
        )

        private var mockActivities: MutableList<Activity> = loadActivities() ?: mutableListOf(
            Activity("Leon submitted Login Screen", "10 mins ago"),
            Activity("PM approved Dashboard UI", "1 hour ago"),
            Activity("New Task assigned to Andi", "2 hours ago"),
            Activity("New Client added", "1 day ago")
        )

        private fun loadProjects(): MutableList<Project>? {
            return try {
                val json = prefs.getString(KEY_PROJECTS, null) ?: return null
                val type = object : com.google.gson.reflect.TypeToken<MutableList<Project>>() {}.type
                val rawList: MutableList<Project> = gson.fromJson(json, type)
                rawList.map { project ->
                    Project(
                        name = project.name ?: "Unnamed Project",
                        client = project.client ?: "Client",
                        progress = project.progress,
                        membersCount = project.membersCount,
                        deadline = project.deadline ?: "30 Jul",
                        deadlineColorHex = project.deadlineColorHex ?: "#2563EB"
                    )
                }.toMutableList()
            } catch (e: Exception) {
                null
            }
        }

        private fun loadTasks(): MutableList<Task>? {
            return try {
                val json = prefs.getString(KEY_TASKS, null) ?: return null
                val type = object : com.google.gson.reflect.TypeToken<MutableList<Task>>() {}.type
                val rawList: MutableList<Task> = gson.fromJson(json, type)
                rawList.map { task ->
                    Task(
                        title = task.title ?: "Unnamed Task",
                        project = task.project ?: "General",
                        priority = task.priority ?: "Low Priority",
                        priorityColorHex = task.priorityColorHex ?: "#10B981",
                        status = task.status ?: "To Do",
                        assignedTo = task.assignedTo ?: "Unassigned",
                        assignedAvatar = task.assignedAvatar,
                        deadline = task.deadline ?: "Select Date",
                        description = task.description ?: ""
                    )
                }.toMutableList()
            } catch (e: Exception) {
                null
            }
        }

        private fun loadActivities(): MutableList<Activity>? {
            return try {
                val json = prefs.getString(KEY_ACTIVITIES, null) ?: return null
                val type = object : com.google.gson.reflect.TypeToken<MutableList<Activity>>() {}.type
                val rawList: MutableList<Activity> = gson.fromJson(json, type)
                rawList.map { activity ->
                    Activity(
                        description = activity.description ?: "Activity",
                        timestamp = activity.timestamp ?: "Just now"
                    )
                }.toMutableList()
            } catch (e: Exception) {
                null
            }
        }

        private fun saveList(key: String, list: List<*>) {
            try {
                val json = gson.toJson(list)
                prefs.edit().putString(key, json).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getProjects(): List<Project> {
        return try {
            api?.getProjects() ?: mockProjects
        } catch (e: Exception) {
            mockProjects
        }
    }

    suspend fun getTasks(): List<Task> {
        return try {
            api?.getTasks() ?: mockTasks
        } catch (e: Exception) {
            mockTasks
        }
    }

    suspend fun addProject(project: Project) {
        mockProjects.add(0, project)
        mockActivities.add(0, Activity("New project '${project.name}' added", "Just now"))
        saveList(KEY_PROJECTS, mockProjects)
        saveList(KEY_ACTIVITIES, mockActivities)
    }

    suspend fun updateProject(oldName: String, name: String, client: String, deadline: String, members: Int) {
        val index = mockProjects.indexOfFirst { it.name == oldName }
        if (index != -1) {
            val oldProject = mockProjects[index]
            val updatedProject = oldProject.copy(
                name = name,
                client = client,
                deadline = deadline,
                membersCount = members
            )
            mockProjects[index] = updatedProject

            // Update tasks project name
            if (oldName != name) {
                mockTasks.forEachIndexed { i, task ->
                    if (task.project == oldName) {
                        mockTasks[i] = task.copy(project = name)
                    }
                }
                saveList(KEY_TASKS, mockTasks)
            }

            mockActivities.add(0, Activity("Project '$oldName' updated to '$name'", "Just now"))
            saveList(KEY_PROJECTS, mockProjects)
            saveList(KEY_ACTIVITIES, mockActivities)
        }
    }

    suspend fun deleteProject(name: String) {
        mockProjects.removeAll { it.name == name }
        mockActivities.add(0, Activity("Project '$name' deleted", "Just now"))
        saveList(KEY_PROJECTS, mockProjects)
        saveList(KEY_ACTIVITIES, mockActivities)
    }

    suspend fun addTask(task: Task) {
        mockTasks.add(0, task)
        mockActivities.add(0, Activity("New task '${task.title}' added to '${task.project}'", "Just now"))
        
        // Update project progress
        updateProjectProgress(task.project)

        saveList(KEY_TASKS, mockTasks)
        saveList(KEY_ACTIVITIES, mockActivities)
    }

    suspend fun updateTaskStatus(taskTitle: String, newStatus: String, newDeadline: String? = null) {
        val index = mockTasks.indexOfFirst { it.title == taskTitle }
        if (index != -1) {
            val oldTask = mockTasks[index]
            val updatedTask = oldTask.copy(
                status = newStatus,
                deadline = newDeadline ?: oldTask.deadline
            )
            mockTasks[index] = updatedTask
            mockActivities.add(0, Activity("Task '$taskTitle' updated", "Just now"))
            
            // Update project progress
            updateProjectProgress(updatedTask.project)
            
            saveList(KEY_TASKS, mockTasks)
            saveList(KEY_ACTIVITIES, mockActivities)
        }
    }

    private fun updateProjectProgress(projectName: String) {
        val projectTasks = mockTasks.filter { it.project == projectName }
        if (projectTasks.isEmpty()) return

        val doneTasks = projectTasks.count { it.status.equals("Done", ignoreCase = true) }
        val newProgress = (doneTasks.toFloat() / projectTasks.size * 100).toInt()

        val projectIndex = mockProjects.indexOfFirst { it.name == projectName }
        if (projectIndex != -1) {
            mockProjects[projectIndex] = mockProjects[projectIndex].copy(progress = newProgress)
            saveList(KEY_PROJECTS, mockProjects)
        }
    }

    suspend fun getActivities(): List<Activity> {
        return try {
            api?.getActivities() ?: mockActivities
        } catch (e: Exception) {
            mockActivities
        }
    }

    private suspend fun getMockProjects(): List<Project> = mockProjects
    private suspend fun getMockTasks(): List<Task> = mockTasks
    private suspend fun getMockActivities(): List<Activity> = mockActivities
}
