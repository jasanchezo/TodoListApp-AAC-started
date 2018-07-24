package com.mtw.juancarlos.todolistapp.database

import android.arch.persistence.room.TypeConverter
import java.util.*

// TODO (6) Crear TypeConverter

class DateConverter {

    @TypeConverter
    fun toDate(timeStamp : Long?) : Date? {
        return if (timeStamp != null) Date(timeStamp) else null
    }

    @TypeConverter
    fun toTimeStamp(date : Date?) : Long? = date?.time
}