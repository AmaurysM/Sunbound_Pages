package com.amaurysdelossantos.project.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

actual class PermissionsManager actual constructor(
    private val callback: PermissionCallback
) : PermissionHandler {

    @Composable
    actual override fun askPermission(permission: PermissionType) {
        // On desktop, permissions are always granted (for local files)
        callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
    }

    @Composable
    actual override fun isPermissionGranted(permission: PermissionType): Boolean {
        // Always granted on desktop
        return true
    }

    @Composable
    actual override fun launchSettings() {
        // No settings on desktop, no-op
    }
}

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    // Simply return the PermissionsManager instance
    return remember { PermissionsManager(callback) }
}