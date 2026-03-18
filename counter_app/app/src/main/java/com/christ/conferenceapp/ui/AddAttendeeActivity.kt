package com.christ.conferenceapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.christ.conferenceapp.R
import com.christ.conferenceapp.data.AppDatabase
import com.christ.conferenceapp.data.Attendee
import com.christ.conferenceapp.databinding.ActivityAddAttendeeBinding
import com.christ.conferenceapp.repository.AttendeeRepository
import com.christ.conferenceapp.viewmodel.AttendeeViewModel
import com.google.android.material.snackbar.Snackbar

class AddAttendeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAttendeeBinding
    private lateinit var viewModel: AttendeeViewModel

    private var pendingAttendee: Attendee? = null

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.values.all { it }
            if (granted) {
                pendingAttendee?.let(::registerAttendee)
            } else {
                Snackbar.make(binding.root, R.string.sms_permission_denied, Snackbar.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAttendeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AttendeeRepository(AppDatabase.getInstance(this).attendeeDao())
        viewModel = ViewModelProvider(this, AttendeeViewModel.Factory(repository))[AttendeeViewModel::class.java]

        setupToolbar()

        binding.registerButton.setOnClickListener {
            submitRegistration()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_attendee_title)
        binding.topAppBar.setNavigationOnClickListener { finish() }
    }

    private fun submitRegistration() {
        val attendee = validateForm() ?: return
        pendingAttendee = attendee

        if (!deviceCanSendSms()) {
            Snackbar.make(binding.root, R.string.sms_not_supported, Snackbar.LENGTH_LONG).show()
            return
        }

        if (hasSmsPermissions()) {
            registerAttendee(attendee)
        } else {
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
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

        return Attendee(name = name, age = age, phoneNumber = phoneNumber)
    }

    private fun registerAttendee(attendee: Attendee) {
        binding.registerButton.isEnabled = false
        viewModel.addAttendee(
            attendee = attendee,
            onSuccess = {
                val message = runCatching {
                    sendConfirmationSms(attendee)
                    getString(R.string.attendee_registered)
                }.getOrElse {
                    getString(R.string.sms_failed_after_save)
                }
                setResult(RESULT_OK, Intent().putExtra(MainActivity.EXTRA_STATUS_MESSAGE, message))
                finish()
            },
            onError = {
                binding.registerButton.isEnabled = true
                Snackbar.make(binding.root, R.string.registration_failed, Snackbar.LENGTH_LONG).show()
            }
        )
    }

    private fun sendConfirmationSms(attendee: Attendee) {
        val smsBody = getString(R.string.sms_message_template, attendee.name)
        val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(SmsManager::class.java)
        } else {
            @Suppress("DEPRECATION")
            SmsManager.getDefault()
        } ?: error("SmsManager unavailable")

        smsManager.sendTextMessage(attendee.phoneNumber, null, smsBody, null, null)
    }

    private fun hasSmsPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun deviceCanSendSms(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_MESSAGING)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE
        )
        private val PHONE_NUMBER_REGEX = Regex("^\\d{10}$")
    }
}
