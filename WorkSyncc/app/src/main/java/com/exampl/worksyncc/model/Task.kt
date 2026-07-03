package com.exampl.worksyncc.model

data class Task(
    val title: String,
    val project: String,
    val priority: String,
    val priorityColorHex: String,
    var status: String = "To Do",
    val assignedTo: String = "Unassigned",
    val assignedAvatar: String? = null,
    var deadline: String = "Select Date",
    var description: String = ""
)
