package com.exampl.worksyncc.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exampl.worksyncc.model.Project
import com.exampl.worksyncc.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel : ViewModel() {
    private val repository = ProjectRepository()

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> = _projects

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadProjects() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getProjects()
                _projects.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
