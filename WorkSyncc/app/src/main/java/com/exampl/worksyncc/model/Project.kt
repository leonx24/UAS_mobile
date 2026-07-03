package com.exampl.worksyncc.model

data class Project(
    val name: String,
    val client: String,
    val progress: Int,
    val membersCount: Int,
    val deadline: String,
    val deadlineColorHex: String
)
