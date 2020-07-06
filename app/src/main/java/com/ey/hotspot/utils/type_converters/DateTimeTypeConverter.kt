package com.ey.hotspot.utils.type_converters

import androidx.room.TypeConverter
import java.util.*

object TimeTypeConverters {
    @TypeConverter
    @JvmStatic
    fun toCalendar(l: Long?): Calendar? =
        if (l == null) null else Calendar.getInstance().apply { timeInMillis = l }

    @TypeConverter
    @JvmStatic
    fun fromCalendar(c: Calendar?): Long? = c?.time?.time
}