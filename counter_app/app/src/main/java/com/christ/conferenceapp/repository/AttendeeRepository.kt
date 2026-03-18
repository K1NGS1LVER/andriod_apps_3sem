package com.christ.conferenceapp.repository

import androidx.lifecycle.LiveData
import com.christ.conferenceapp.data.Attendee
import com.christ.conferenceapp.data.AttendeeDao

class AttendeeRepository(private val attendeeDao: AttendeeDao) {

    fun getAllAttendees(): LiveData<List<Attendee>> = attendeeDao.getAllAttendees()

    suspend fun insertAttendee(attendee: Attendee): Long = attendeeDao.insertAttendee(attendee)

    suspend fun updateAttendee(attendee: Attendee) = attendeeDao.updateAttendee(attendee)

    suspend fun deleteAttendee(attendee: Attendee) = attendeeDao.deleteAttendee(attendee)
}
