package com.christ.conferenceapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.christ.conferenceapp.R
import com.christ.conferenceapp.data.AppDatabase
import com.christ.conferenceapp.data.Attendee
import com.christ.conferenceapp.databinding.ActivityMainBinding
import com.christ.conferenceapp.repository.AttendeeRepository
import com.christ.conferenceapp.viewmodel.AttendeeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AttendeeViewModel
    private lateinit var attendeeAdapter: AttendeeAdapter

    private val addAttendeeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result.resultCode, result.data)
        }

    private val editAttendeeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result.resultCode, result.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AttendeeRepository(AppDatabase.getInstance(this).attendeeDao())
        viewModel = ViewModelProvider(this, AttendeeViewModel.Factory(repository))[AttendeeViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        observeAttendees()

        binding.addAttendeeFab.setOnClickListener {
            addAttendeeLauncher.launch(Intent(this, AddAttendeeActivity::class.java))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = getString(R.string.dashboard_title)
    }

    private fun setupRecyclerView() {
        attendeeAdapter = AttendeeAdapter(
            onEditClick = { attendee ->
                editAttendeeLauncher.launch(EditAttendeeActivity.createIntent(this, attendee))
            },
            onDeleteClick = ::showDeleteConfirmation
        )

        binding.attendeesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = attendeeAdapter
        }
    }

    private fun observeAttendees() {
        viewModel.attendees.observe(this) { attendees ->
            attendeeAdapter.submitList(attendees)
            binding.emptyStateTextView.visibility = if (attendees.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showDeleteConfirmation(attendee: Attendee) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.remove_attendee_title)
            .setMessage(R.string.remove_attendee_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.confirm_delete) { _, _ ->
                viewModel.deleteAttendee(
                    attendee = attendee,
                    onSuccess = {
                        Snackbar.make(binding.root, R.string.attendee_removed, Snackbar.LENGTH_SHORT).show()
                    },
                    onError = {
                        Snackbar.make(binding.root, R.string.delete_failed, Snackbar.LENGTH_LONG).show()
                    }
                )
            }
            .show()
    }

    private fun handleActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        val message = data?.getStringExtra(EXTRA_STATUS_MESSAGE).orEmpty()
        if (message.isNotBlank()) {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_STATUS_MESSAGE = "extra_status_message"
    }
}
