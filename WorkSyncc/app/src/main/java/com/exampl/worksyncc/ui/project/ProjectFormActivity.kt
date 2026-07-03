package com.exampl.worksyncc.ui.project

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exampl.worksyncc.databinding.ActivityProjectFormBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProjectFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectFormBinding
    private var isEditMode = false
    private var projectName: String? = null
    private val repository = com.exampl.worksyncc.repository.DashboardRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectName = intent.getStringExtra("PROJECT_NAME")
        isEditMode = projectName != null

        setupUI(projectName)
        setupListeners()
    }

    private fun setupUI(projectName: String?) {
        if (isEditMode) {
            binding.toolbar.title = "Edit Project"
            binding.btnSave.text = "UPDATE PROJECT"
            binding.edtProjectName.setText(projectName)
            // Mock other fields for edit
            binding.edtClientName.setText("Mock Client")
            binding.edtDeadline.setText("30/7/2024")
            binding.edtMembers.setText("5")
        } else {
            binding.toolbar.title = "Create Project"
            binding.btnSave.text = "SAVE PROJECT"
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.edtDeadline.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            val name = binding.edtProjectName.text.toString()
            val client = binding.edtClientName.text.toString()
            val deadline = binding.edtDeadline.text.toString()
            val members = binding.edtMembers.text.toString().toIntOrNull() ?: 0

            if (name.isEmpty()) {
                binding.edtProjectName.error = "Required"
                return@setOnClickListener
            }
            
            CoroutineScope(Dispatchers.Main).launch {
                if (isEditMode) {
                    repository.updateProject(projectName ?: "", name, client, deadline, members)
                    val resultIntent = android.content.Intent().apply {
                        putExtra("UPDATED_PROJECT_NAME", name)
                    }
                    setResult(RESULT_OK, resultIntent)
                } else {
                    val newProject = com.exampl.worksyncc.model.Project(
                        name, client, 0, members, deadline, "#2563EB"
                    )
                    repository.addProject(newProject)
                }
                
                val message = if (isEditMode) "Project updated!" else "Project created!"
                Toast.makeText(this@ProjectFormActivity, message, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            binding.edtDeadline.setText("$d/${m + 1}/$y")
        }, year, month, day)
        dpd.show()
    }
}
