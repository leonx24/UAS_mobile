package com.exampl.worksyncc.ui.project

import android.view.View
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exampl.worksyncc.adapter.ProjectAdapter
import com.exampl.worksyncc.databinding.ActivityProjectListBinding

class ProjectListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectListBinding
    private val viewModel: ProjectViewModel by viewModels()
    private lateinit var adapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        viewModel.loadProjects()
    }

    private fun setupRecyclerView() {
        adapter = ProjectAdapter(emptyList()) { project ->
            // Navigate to Detail
            val intent = android.content.Intent(this, ProjectDetailActivity::class.java).apply {
                putExtra("PROJECT_NAME", project.name)
            }
            startActivity(intent)
        }
        binding.rvProjects.layoutManager = LinearLayoutManager(this)
        binding.rvProjects.adapter = adapter
    }

    private fun setupListeners() {
        val tokenManager = com.exampl.worksyncc.utils.TokenManager(this)
        val isPM = tokenManager.getUserRole() == "Project Manager"
        binding.fabAddProject.visibility = if (isPM) View.VISIBLE else View.GONE

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadProjects()
        }

        binding.fabAddProject.setOnClickListener {
            val intent = android.content.Intent(this, ProjectFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.projects.observe(this) { projects ->
            adapter.updateData(projects)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
