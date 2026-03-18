package com.christ.conferenceapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.christ.conferenceapp.R
import com.christ.conferenceapp.data.AppDatabase
import com.christ.conferenceapp.data.Attendee
import com.christ.conferenceapp.databinding.ActivityEditAttendeeBinding
import com.christ.conferenceapp.repository.AttendeeRepository
import com.christ.conferenceapp.viewmodel.AttendeeViewModel
import com.google.android.material.snackbar.Snackbar

class EditAttendeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAttendeeBinding
    private lateinit var viewModel: AttendeeViewModel

    private var attendeeId: Int = INVALID_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAttendeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AttendeeRepository(AppDatabase.getInstance(this).attendeeDao())
        viewModel = ViewModelProvider(this, AttendeeViewModel.Factory(repository))[AttendeeViewModel::class.java]

        setupToolbar()
        populateExistingAttendee()

        binding.saveButton.setOnClickListener {
            submitUpdate()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.edit_attendee_title)
        binding.topAppBar.setNavigationOnClickListener { finish() }
    }

    private fun populateExistingAttendee() {
        attendeeId = intent.getIntExtra(EXTRA_ID, INVALID_ID)
        val name = intent.getStringExtra(EXTRA_NAME).orEmpty()
        val age = intent.getIntExtra(EXTRA_AGE, INVALID_ID)
        val phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER).orEmpty()

        if (attendeeId == INVALID_ID || name.isBlank() || age == INVALID_ID || phoneNumber.isBlank()) {
            Snackbar.make(binding.root, R.string.missing_attendee, Snackbar.LENGTH_LONG).show()
            binding.saveButton.isEnabled = false
            return
        }

        binding.nameEditText.setText(name)
        binding.ageEditText.setText(age.toString())
        binding.phoneEditText.setText(phoneNumber)
    }

    private fun submitUpdate() {
        val updatedAttendee = validateForm() ?: return
        binding.saveButton.isEnabled = false

        viewModel.updateAttendee(
            attendee = updatedAttendee,
            onSuccess = {
                setResult(
                    RESULT_OK,
                    Intent().putExtra(MainActivity.EXTRA_STATUS_MESSAGE, getString(R.string.attendee_updated))
                )
                finish()
            },
            onError = {
                binding.saveButton.isEnabled = true
                Snackbar.make(binding.root, R.string.update_failed, Snackbar.LENGTH_LONG).show()
            }
        )
    }

    private fun validateForm(): Attendee? {
        binding.nameLayout.error = null
        binding.ageLayout.error = null
        binding.phoneLayout.error = null

        val name = binding.nameEditText.text?.toString()?.trim().orEmpty()
        val ageInput = binding.ageEditText.text?.toString()?.trim().orEmpty()
        val phoneNumber = binding.phoneEditText.text?.toString()?.trim().orEmpty()

        if (name.isBlank()) {
            binding.nameLayout.error = getString(R.string.name_required)
            return null
        }

        if (ageInput.isBlank()) {
            binding.ageLayout.error = getString(R.string.age_required)
            return null
        }

        val age = ageInput.toIntOrNull()
        if (age == null) {
            binding.ageLayout.error = getString(R.string.age_invalid)
            return null
        }

        if (age !in 1..120) {
            binding.ageLayout.error = getString(R.string.age_range_invalid)
            return null
        }

        if (phoneNumber.isBlank()) {
            binding.phoneLayout.error = getString(R.string.phone_required)
            return null
        }

        if (!PHONE_NUMBER_REGEX.matches(phoneNumber)) {
            binding.phoneLayout.error = getString(R.string.phone_invalid)
            return null
        }

        return Attendee(
            id = attendeeId,
            name = name,
            age = age,
            phoneNumber = phoneNumber
        )
    }

    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_NAME = "extra_name"
        private const val EXTRA_AGE = "extra_age"
        private const val EXTRA_PHONE_NUMBER = "extra_phone_number"
        private const val INVALID_ID = -1
        private val PHONE_NUMBER_REGEX = Regex("^\\d{10}$")

        fun createIntent(context: Context, attendee: Attendee): Intent {
            return Intent(context, EditAttendeeActivity::class.java).apply {
                putExtra(EXTRA_ID, attendee.id)
                putExtra(EXTRA_NAME, attendee.name)
                putExtra(EXTRA_AGE, attendee.age)
                putExtra(EXTRA_PHONE_NUMBER, attendee.phoneNumber)
            }
        }
    }
}
