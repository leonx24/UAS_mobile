package com.exampl.worksyncc.ui.task

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exampl.worksyncc.adapter.TaskAdapter
import com.exampl.worksyncc.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        viewModel.loadTasks()
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(emptyList()) { task ->
            val intent = android.content.Intent(this, TaskDetailActivity::class.java).apply {
                putExtra("TASK_TITLE", task.title)
                putExtra("PROJECT_NAME", task.project)
            }
            startActivity(intent)
        }
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter
    }

    private fun setupListeners() {
        val tokenManager = com.exampl.worksyncc.utils.TokenManager(this)
        val isPM = tokenManager.getUserRole() == "Project Manager"
        binding.fabAddTask.visibility = if (isPM) android.view.View.VISIBLE else android.view.View.GONE

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadTasks()
        }

        binding.fabAddTask.setOnClickListener {
            val intent = android.content.Intent(this, TaskFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.tasks.observe(this) { tasks ->
            adapter.updateData(tasks)
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
