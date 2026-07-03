package com.exampl.worksyncc.ui.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exampl.worksyncc.adapter.TaskAdapter
import com.exampl.worksyncc.databinding.ActivityProjectDetailBinding
import com.exampl.worksyncc.repository.DashboardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetailBinding
    private val repository = DashboardRepository()
    private var currentProjectName: String = ""
    private lateinit var taskAdapter: TaskAdapter

    private val editProjectLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedName = result.data?.getStringExtra("UPDATED_PROJECT_NAME")
            if (updatedName != null) {
                currentProjectName = updatedName
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentProjectName = intent.getStringExtra("PROJECT_NAME") ?: "Project Detail"
        
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        loadProjectData(currentProjectName)
    }

    private fun loadProjectData(projectName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val project = repository.getProjects().find { it.name == projectName }
            if (project != null) {
                binding.toolbar.title = project.name
                binding.txtProjectName.text = project.name
                binding.txtClientName.text = project.client
                binding.pbProject.progress = project.progress
                binding.txtProgressPercent.text = "${project.progress}%"
                binding.txtDeadline.text = project.deadline
                binding.txtMembers.text = "${project.membersCount} Members"

                // Load tasks related to this project dynamically
                val projectTasks = repository.getTasks().filter { it.project == projectName }
                taskAdapter = TaskAdapter(projectTasks) { task ->
                    val intent = Intent(this@ProjectDetailActivity, com.exampl.worksyncc.ui.task.TaskDetailActivity::class.java).apply {
                        putExtra("TASK_TITLE", task.title)
                        putExtra("PROJECT_NAME", task.project)
                        putExtra("TASK_STATUS", task.status)
                    }
                    startActivity(intent)
                }
                binding.rvProjectTasks.layoutManager = LinearLayoutManager(this@ProjectDetailActivity)
                binding.rvProjectTasks.adapter = taskAdapter
            } else {
                Toast.makeText(this@ProjectDetailActivity, "Project data not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupListeners() {
        val tokenManager = com.exampl.worksyncc.utils.TokenManager(this)
        val isPM = tokenManager.getUserRole() == "Project Manager"
        
        binding.btnEdit.visibility = if (isPM) View.VISIBLE else View.GONE
        binding.btnDelete.visibility = if (isPM) View.VISIBLE else View.GONE
        binding.btnAddTask.visibility = if (isPM) View.VISIBLE else View.GONE

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, ProjectFormActivity::class.java).apply {
                putExtra("PROJECT_NAME", currentProjectName)
            }
            editProjectLauncher.launch(intent)
        }

        binding.btnDelete.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Project")
                .setMessage("Are you sure you want to delete this project?")
                .setPositiveButton("Delete") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        repository.deleteProject(currentProjectName)
                        Toast.makeText(this@ProjectDetailActivity, "Project deleted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.btnAddTask.setOnClickListener {
            val intent = Intent(this, com.exampl.worksyncc.ui.task.TaskFormActivity::class.java).apply {
                putExtra("PRE_SELECTED_PROJECT", currentProjectName)
            }
            startActivity(intent)
        }
    }
}
