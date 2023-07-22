package com.eltescode.estimations.core.database

import androidx.room.TypeConverter
import com.eltescode.estimations.features.estimation.data.local.model.TreeCached
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TreeCachedConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromTreeCached(data: List<TreeCached>): String {
            return Gson().toJson(data)
        }

        @TypeConverter
        @JvmStatic
        fun toTreeCached(json: String): List<TreeCached> {
            return Gson().fromJson(json, object : TypeToken<List<TreeCached>>() {}.type)
        }
    }
}