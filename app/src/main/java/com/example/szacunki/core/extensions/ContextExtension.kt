package com.example.szacunki.core.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.szacunki.R

fun Context.hasLocationPermission(): Boolean {
    return (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )) == PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    )) == PackageManager.PERMISSION_GRANTED
}

fun Context.getBaseDiameterList() = this.resources.getStringArray(R.array.baseDiameterList).toList()
fun Context.getBaseNameList() = this.resources.getStringArray(R.array.baseNameList).toList()
fun Context.showShortHint(id: Int) {
    Toast.makeText(this, getString(id), Toast.LENGTH_LONG).show()
}

fun Context.showLongHint(id: Int) {
    Toast.makeText(this, getString(id), Toast.LENGTH_LONG).show()
}

fun Context.showLongHint(id: Int, text: String) {
    Toast.makeText(this, getString(id, text), Toast.LENGTH_LONG).show()
}