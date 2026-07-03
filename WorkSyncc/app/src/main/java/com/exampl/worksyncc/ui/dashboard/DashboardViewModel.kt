package com.exampl.worksyncc.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exampl.worksyncc.model.Activity
import com.exampl.worksyncc.model.Project
import com.exampl.worksyncc.model.Task
import com.exampl.worksyncc.repository.DashboardRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: DashboardRepository = DashboardRepository()) : ViewModel() {

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> = _projects

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _filteredTasks = MutableLiveData<List<Task>>()
    val filteredTasks: LiveData<List<Task>> = _filteredTasks

    private var allProjects: List<Project> = emptyList()
    private var allTasks: List<Task> = emptyList()

    private var currentSearchQuery: String = ""
    private var currentPriorityFilter: String = "All"

    private val _activities = MutableLiveData<List<Activity>>()
    val activities: LiveData<List<Activity>> = _activities

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadDashboardData(isRefresh: Boolean = false) {
        if (isRefresh) {
            _isRefreshing.value = true
        } else {
            _isLoading.value = true
        }
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // Fetch concurrently
                val projectsDeferred = async { repository.getProjects() }
                val tasksDeferred = async { repository.getTasks() }
                val activitiesDeferred = async { repository.getActivities() }

                allProjects = projectsDeferred.await()
                allTasks = tasksDeferred.await()
                
                applyFilters()
                _projects.value = allProjects
                _activities.value = activitiesDeferred.await()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to load dashboard data"
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

    fun search(query: String) {
        currentSearchQuery = query
        applyFilters()
        
        // Projects are always displayed fully, unaffected by the dashboard search
        _projects.value = allProjects
    }

    fun filterTasksByPriority(priority: String) {
        currentPriorityFilter = priority
        applyFilters()
    }

    private fun applyFilters() {
        val lowerQuery = currentSearchQuery.lowercase()

        // Projects are not affected by search filters
        _projects.value = allProjects

        // Filter Tasks with both Search and Priority
        var filteredTasks = allTasks
        if (currentPriorityFilter != "All") {
            filteredTasks = filteredTasks.filter { it.priority.contains(currentPriorityFilter, ignoreCase = true) }
        }
        if (currentSearchQuery.isNotEmpty()) {
            filteredTasks = filteredTasks.filter {
                it.title.lowercase().contains(lowerQuery) ||
                it.project.lowercase().contains(lowerQuery)
            }
        }
        _tasks.value = filteredTasks
    }

    fun updateTaskStatus(taskTitle: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskTitle, newStatus)
            loadDashboardData(isRefresh = true) // Reload to reflect changes
        }
    }
}
