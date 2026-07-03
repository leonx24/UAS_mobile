package com.exampl.worksyncc.ui.task

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exampl.worksyncc.databinding.ActivityTaskFormBinding
import com.exampl.worksyncc.repository.DashboardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding
    private val repository = DashboardRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()
        setupListeners()
    }

    private fun setupSpinners() {
        // Load projects list dynamically from repository (not hardcoded)
        CoroutineScope(Dispatchers.Main).launch {
            val projects = repository.getProjects()
            val projectNames = projects.map { it.name }.toTypedArray()
            val projectAdapter = ArrayAdapter(this@TaskFormActivity, android.R.layout.simple_dropdown_item_1line, projectNames)
            binding.spinnerProject.setAdapter(projectAdapter)

            // Pre-select project if passed from Project Detail page
            val preSelected = intent.getStringExtra("PRE_SELECTED_PROJECT")
            if (preSelected != null) {
                binding.spinnerProject.setText(preSelected, false)
            }
        }

        val priorities = arrayOf("High Priority", "Medium Priority", "Low Priority")
        val priorityAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, priorities)
        binding.spinnerPriority.setAdapter(priorityAdapter)
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.edtTaskTitle.text.toString()
            val project = binding.spinnerProject.text.toString()
            val priority = binding.spinnerPriority.text.toString()

            if (title.isEmpty()) {
                binding.edtTaskTitle.error = "Required"
                return@setOnClickListener
            }
            if (project.isEmpty()) {
                binding.spinnerProject.error = "Required"
                return@setOnClickListener
            }
            if (priority.isEmpty()) {
                binding.spinnerPriority.error = "Required"
                return@setOnClickListener
            }

            val priorityColor = when (priority) {
                "High Priority" -> "#EF4444"
                "Medium Priority" -> "#F59E0B"
                else -> "#10B981"
            }

            val description = binding.edtDescription.text.toString()

            val newTask = com.exampl.worksyncc.model.Task(
                title = title,
                project = project,
                priority = priority,
                priorityColorHex = priorityColor,
                status = "To Do",
                assignedTo = "Leon",
                description = description
            )

            CoroutineScope(Dispatchers.Main).launch {
                repository.addTask(newTask)
                Toast.makeText(this@TaskFormActivity, "Task created successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
