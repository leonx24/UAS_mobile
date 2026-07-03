package com.exampl.worksyncc.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.exampl.worksyncc.LoginActivity
import com.exampl.worksyncc.R
import com.exampl.worksyncc.adapter.ActivityAdapter
import com.exampl.worksyncc.adapter.ProjectAdapter
import com.exampl.worksyncc.adapter.TaskAdapter
import com.exampl.worksyncc.databinding.ActivityDashboardBinding

import java.util.Calendar

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by lazy { DashboardViewModel() }
    private lateinit var tokenManager: com.exampl.worksyncc.utils.TokenManager
    
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var activityAdapter: ActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = com.exampl.worksyncc.utils.TokenManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupUserUI()
        setupRecyclerViews()
        setupBottomNavigation()
        setupSwipeRefresh()
        setupSearch()
        setupFilters()
        setupLogout()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        // Auto-refresh when returning to Dashboard to display changes (new/edited projects/tasks status)
        viewModel.loadDashboardData(isRefresh = true)
    }

    private fun setupUserUI() {
        // Dynamic Greeting
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 0..11 -> "Good Morning,"
            in 12..15 -> "Good Afternoon,"
            in 16..20 -> "Good Evening,"
            else -> "Good Night,"
        }
        binding.txtMorning.text = greeting

        // User Data
        val userName = tokenManager.getUserName() ?: "User"
        val userRole = tokenManager.getUserRole() ?: "Employee"

        binding.txtName.text = userName
        binding.txtRole.text = userRole

        // Populate Profile Tab Info Card dynamically
        binding.txtProfileName.text = userName
        binding.txtProfileRole.text = userRole
        binding.txtProfileEmail.text = if (userName.lowercase() == "leon") "leon@worksync.com" else "employee@worksync.com"

        // Role Based Visibility
        val isPM = userRole == "Project Manager"
        binding.fabAdd.visibility = if (isPM) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerViews() {
        // Active projects list
        projectAdapter = ProjectAdapter(emptyList()) { project ->
            val intent = Intent(this, com.exampl.worksyncc.ui.project.ProjectDetailActivity::class.java).apply {
                putExtra("PROJECT_NAME", project.name)
            }
            startActivity(intent)
        }
        binding.rvActiveProjects.layoutManager = LinearLayoutManager(this)
        binding.rvActiveProjects.adapter = projectAdapter

        // All projects tab list
        binding.rvAllProjects.layoutManager = LinearLayoutManager(this)
        binding.rvAllProjects.adapter = ProjectAdapter(emptyList()) { project ->
            val intent = Intent(this, com.exampl.worksyncc.ui.project.ProjectDetailActivity::class.java).apply {
                putExtra("PROJECT_NAME", project.name)
            }
            startActivity(intent)
        }

        // Today's tasks list
        taskAdapter = TaskAdapter(emptyList()) { task ->
            val intent = Intent(this, com.exampl.worksyncc.ui.task.TaskDetailActivity::class.java).apply {
                putExtra("TASK_TITLE", task.title)
                putExtra("PROJECT_NAME", task.project)
                putExtra("TASK_STATUS", task.status)
            }
            startActivity(intent)
        }
        binding.rvTodaysTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTodaysTasks.adapter = taskAdapter

        // All tasks tab list
        binding.rvAllTasks.layoutManager = LinearLayoutManager(this)
        binding.rvAllTasks.adapter = TaskAdapter(emptyList()) { task ->
            val intent = Intent(this, com.exampl.worksyncc.ui.task.TaskDetailActivity::class.java).apply {
                putExtra("TASK_TITLE", task.title)
                putExtra("PROJECT_NAME", task.project)
                putExtra("TASK_STATUS", task.status)
            }
            startActivity(intent)
        }

        // Recent Activity list
        activityAdapter = ActivityAdapter(emptyList())
        binding.rvRecentActivity.layoutManager = LinearLayoutManager(this)
        binding.rvRecentActivity.adapter = activityAdapter
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showScreen(R.id.nav_home)
                    true
                }
                R.id.nav_projects -> {
                    showScreen(R.id.nav_projects)
                    true
                }
                R.id.nav_tasks -> {
                    showScreen(R.id.nav_tasks)
                    true
                }
                R.id.nav_profile -> {
                    showScreen(R.id.nav_profile)
                    true
                }
                else -> false
            }
        }
    }

    private fun showScreen(screenId: Int) {
        binding.layoutHome.visibility = if (screenId == R.id.nav_home) View.VISIBLE else View.GONE
        binding.layoutProjects.visibility = if (screenId == R.id.nav_projects) View.VISIBLE else View.GONE
        binding.layoutTasks.visibility = if (screenId == R.id.nav_tasks) View.VISIBLE else View.GONE
        binding.layoutProfile.visibility = if (screenId == R.id.nav_profile) View.VISIBLE else View.GONE
        
        // Role based FAB
        val isPM = tokenManager.getUserRole() == "Project Manager"
        binding.fabAdd.visibility = if (screenId == R.id.nav_home && isPM) View.VISIBLE else View.GONE
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadDashboardData(isRefresh = true)
        }
    }

    private fun setupSearch() {
        binding.edtSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.search(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun setupFilters() {
        binding.chipGroupPriority.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: R.id.chipAll
            val priority = when (checkedId) {
                R.id.chipHigh -> "High"
                R.id.chipMedium -> "Medium"
                R.id.chipLow -> "Low"
                else -> "All"
            }
            viewModel.filterTasksByPriority(priority)
        }
    }

    private fun setupLogout() {
        binding.txtRecentActivityTitle.setOnClickListener {
            val intent = Intent(this, com.exampl.worksyncc.ui.notification.NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.imgProfile.setOnClickListener {
            val intent = Intent(this, com.exampl.worksyncc.ui.profile.ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.itemEditProfile.setOnClickListener {
            val intent = Intent(this, com.exampl.worksyncc.ui.profile.ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.itemNotifications.setOnClickListener {
            val intent = Intent(this, com.exampl.worksyncc.ui.notification.NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.itemPrivacy.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Privacy & Security Policy")
                .setMessage("Your account and project data are secured locally on this device using encrypted token stores (TokenManager). WorkSync ensures all communications with back-end database servers remain encrypted.\n\nVersion: 1.0.0-Stable")
                .setPositiveButton("OK", null)
                .show()
        }

        binding.fabAdd.setOnClickListener {
            val options = arrayOf("New Project", "New Task")
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select Action")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            val intent = Intent(this, com.exampl.worksyncc.ui.project.ProjectFormActivity::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(this, com.exampl.worksyncc.ui.task.TaskFormActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
                .show()
        }
    }

    private fun observeViewModel() {
        viewModel.projects.observe(this) { projects ->
            projectAdapter.updateData(projects)
            (binding.rvAllProjects.adapter as? ProjectAdapter)?.updateData(projects)
            binding.txtProjectsCount.text = projects.size.toString()
        }

        viewModel.tasks.observe(this) { tasks ->
            taskAdapter.updateData(tasks)
            (binding.rvAllTasks.adapter as? TaskAdapter)?.updateData(tasks)
            
            // Update stats cards dynamically
            binding.txtTasksCount.text = tasks.count { it.status != "Done" }.toString() // Active Tasks
            binding.txtReviewCount.text = tasks.count { it.status == "In Progress" }.toString() // In Progress
            binding.txtCompletedCount.text = tasks.count { it.status == "Done" }.toString() // Completed Tasks
        }

        viewModel.activities.observe(this) { activities ->
            activityAdapter.updateData(activities)
        }

        viewModel.isRefreshing.observe(this) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
