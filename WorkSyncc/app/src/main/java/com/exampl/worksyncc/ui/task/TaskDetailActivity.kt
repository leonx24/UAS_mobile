package com.exampl.worksyncc.ui.task

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exampl.worksyncc.databinding.ActivityTaskDetailBinding
import com.exampl.worksyncc.repository.DashboardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding
    private val repository = DashboardRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskTitle = intent.getStringExtra("TASK_TITLE") ?: "Task Detail"

        loadTaskData(taskTitle)
        setupListeners()
    }

    private fun loadTaskData(taskTitle: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val task = repository.getTasks().find { it.title == taskTitle }
            if (task != null) {
                binding.toolbar.title = "Task Detail"
                binding.txtTaskTitle.text = task.title
                binding.txtProjectName.text = task.project
                binding.txtPriority.text = task.priority
                binding.txtDeadline.text = task.deadline
                binding.txtDescription.text = task.description.ifEmpty { "No description provided." }

                // Select status toggle according to current status
                val buttonId = when (task.status) {
                    "In Progress" -> binding.btnDoing.id
                    "Done" -> binding.btnDone.id
                    else -> binding.btnTodo.id
                }
                binding.statusToggleGroup.check(buttonId)
            } else {
                Toast.makeText(this@TaskDetailActivity, "Task data not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private val getFileLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            val fileName = getFileName(uri)
            binding.txtFileName.visibility = android.view.View.VISIBLE
            binding.txtFileName.text = fileName
            Toast.makeText(this, "File attached: $fileName", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileName(uri: android.net.Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "attached_file"
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.txtDeadline.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            android.app.DatePickerDialog(this, { _, year, month, day ->
                binding.txtDeadline.text = "$day/${month + 1}/$year"
            }, calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnAttach.setOnClickListener {
            getFileLauncher.launch("*/*")
        }

        binding.btnSubmit.setOnClickListener {
            val status = when (binding.statusToggleGroup.checkedButtonId) {
                binding.btnTodo.id -> "To Do"
                binding.btnDoing.id -> "In Progress"
                binding.btnDone.id -> "Done"
                else -> "Unknown"
            }
            val deadline = binding.txtDeadline.text.toString()
            
            // Perform safe update synchronously inside CoroutineScope
            CoroutineScope(Dispatchers.Main).launch {
                repository.updateTaskStatus(binding.txtTaskTitle.text.toString(), status, deadline)

                // Show system notification
                com.exampl.worksyncc.utils.NotificationUtils.showNotification(
                    this@TaskDetailActivity,
                    "Task Updated",
                    "Status for task '${binding.txtTaskTitle.text}' is now $status with deadline $deadline"
                )

                Toast.makeText(this@TaskDetailActivity, "Task updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
