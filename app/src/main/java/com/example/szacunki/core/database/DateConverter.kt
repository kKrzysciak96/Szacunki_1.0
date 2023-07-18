package com.example.szacunki.core.database

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date): Long {
            return date.time
        }
        @TypeConverter
        @JvmStatic
        fun toDate(millisSinceEpoch: Long): Date {
            return Date(millisSinceEpoch)
        }
    }
}