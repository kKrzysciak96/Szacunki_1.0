package com.example.szacunki.core.database

import androidx.room.TypeConverter
import com.example.szacunki.features.estimation.data.local.model.TreeRowCached
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TreeRowCachedConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromTreeRowCached(data: List<TreeRowCached>): String {
            return Gson().toJson(data)
        }

        @TypeConverter
        @JvmStatic
        fun toTreeRowCached(json: String): List<TreeRowCached> {
            return Gson().fromJson(json, object : TypeToken<List<TreeRowCached>>() {}.type)
        }
    }
}