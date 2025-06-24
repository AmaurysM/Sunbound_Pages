package com.amaurysdelossantos.project.util

import androidx.compose.runtime.Composable

@Suppress("-Xexpect-actual-classes")
expect class PermissionsManager(callback: PermissionCallback) : PermissionHandler {
    @Composable
    override fun askPermission(permission: PermissionType)

    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean

    @Composable
    override fun launchSettings()
}

interface PermissionCallback {
    fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus)
}

@Composable
expect fun createPermissionsManager(callback: PermissionCallback): PermissionsManager

interface PermissionHandler {

    @Composable
    fun askPermission(permission: PermissionType)

    @Composable
    fun isPermissionGranted(permission: PermissionType): Boolean

    @Composable
    fun launchSettings()

}

enum class PermissionType {
    GALLERY,
    DOCUMENT
}

enum class PermissionStatus {
    GRANTED,
    DENIED,
    SHOW_RATIONALE
}