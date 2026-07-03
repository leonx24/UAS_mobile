package com.exampl.worksyncc.api

import com.exampl.worksyncc.model.Activity
import com.exampl.worksyncc.model.Project
import com.exampl.worksyncc.model.Task
import retrofit2.http.GET

interface DashboardApi {
    @GET("projects")
    suspend fun getProjects(): List<Project>

    @GET("tasks")
    suspend fun getTasks(): List<Task>

    @GET("activities")
    suspend fun getActivities(): List<Activity>
}
