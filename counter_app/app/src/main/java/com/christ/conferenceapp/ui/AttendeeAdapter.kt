package com.christ.conferenceapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.christ.conferenceapp.R
import com.christ.conferenceapp.data.Attendee
import com.christ.conferenceapp.databinding.ItemAttendeeBinding

class AttendeeAdapter(
    private val onEditClick: (Attendee) -> Unit,
    private val onDeleteClick: (Attendee) -> Unit
) : ListAdapter<Attendee, AttendeeAdapter.AttendeeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeeViewHolder {
        val binding = ItemAttendeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendeeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AttendeeViewHolder(
        private val binding: ItemAttendeeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(attendee: Attendee) {
            binding.nameTextView.text = attendee.name
            binding.ageTextView.text = binding.root.context.getString(R.string.age_display, attendee.age)
            binding.phoneTextView.text =
                binding.root.context.getString(R.string.phone_number_display, attendee.phoneNumber)
            binding.editButton.setOnClickListener { onEditClick(attendee) }
            binding.deleteButton.setOnClickListener { onDeleteClick(attendee) }
        }
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Attendee>() {
        override fun areItemsTheSame(oldItem: Attendee, newItem: Attendee): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Attendee, newItem: Attendee): Boolean = oldItem == newItem
    }
}
