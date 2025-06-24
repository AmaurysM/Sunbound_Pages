package com.amaurysdelossantos.project.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return remember { PermissionsManager(callback) }
}

actual class PermissionsManager actual constructor(private val callback: PermissionCallback) :
    PermissionHandler {
    @Composable
    actual override fun askPermission(permission: PermissionType) {
        callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
    }

    @Composable
    actual override fun isPermissionGranted(permission: PermissionType): Boolean {
        return true
    }

    @Composable
    actual override fun launchSettings() {
        val context = LocalContext.current
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        context.startActivity(intent)
    }
//    @Composable
//    actual override fun askPermission(permission: PermissionType) {
//        TODO("Not yet implemented")
//    }
//
//    @Composable
//    override fun isPermissionGranted(permission: PermissionType): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    @Composable
//    override fun launchSettings() {
//        TODO("Not yet implemented")
//    }
}