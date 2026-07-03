package com.exampl.worksyncc.ui.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exampl.worksyncc.adapter.ActivityAdapter
import com.exampl.worksyncc.databinding.ActivityNotificationBinding
import com.exampl.worksyncc.repository.DashboardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private val repository = DashboardRepository() // Using existing for mock data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadNotifications()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        binding.rvNotifications.layoutManager = LinearLayoutManager(this)
    }

    private fun loadNotifications() {
        CoroutineScope(Dispatchers.Main).launch {
            val notifications = withContext(Dispatchers.IO) {
                repository.getActivities()
            }
            binding.rvNotifications.adapter = ActivityAdapter(notifications)
        }
    }
}
