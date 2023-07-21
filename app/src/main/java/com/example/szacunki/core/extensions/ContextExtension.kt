package com.example.szacunki.core.extensions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.szacunki.R


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

fun Context.shareFile(uri: Uri) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "application/pdf"
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    ContextCompat.startActivity(this, intent, null)
}

fun Context.hasLocationPermission(): Boolean {
    return (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )) == PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    )) == PackageManager.PERMISSION_GRANTED
}
