package com.melitopolcherry.timester.core.permissions

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class AndroidPermissionsManager(
    private val fragment: Fragment
) {
    private lateinit var granted: (permissions: List<Permission>) -> Unit
    private var denied: ((permissions: List<Permission>) -> Unit)? = null
    private var rationale: ((permissions: List<Permission>) -> Unit)? = null

    private val requestPermissions = fragment.registerForActivityResult(RequestMultiplePermissions()) { results ->
        val permissions = results.keys.toTypedArray()
        val grantResults = results.values.toBooleanArray()

        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (i in permissions.indices) {
            shouldShowRequestPermissionRationale[i] = fragment.shouldShowRequestPermissionRationale(permissions[i])
        }

        onRequestPermissionsResult(
            permissions = permissions,
            grantResults = grantResults,
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
        )
    }

    fun requestPermissions(
        permissions: Array<String>,
        denied: ((permissions: List<Permission>) -> Unit)? = null,
        rationale: ((permissions: List<Permission>) -> Unit)? = null,
        granted: (permissions: List<Permission>) -> Unit
    ): Boolean {
        this.granted = granted
        this.denied = denied
        this.rationale = rationale
        val needToAskPermissions = permissions.any { permission ->
            val permissionGrantStatus = ActivityCompat.checkSelfPermission(fragment.requireContext(), permission)
            permissionGrantStatus != PackageManager.PERMISSION_GRANTED
        }
        if (needToAskPermissions) {
            requestPermissions.launch(permissions)
        } else {
            onRequestPermissionsResult(
                permissions,
                BooleanArray(permissions.size).apply {
                    fill(true)
                },
                BooleanArray(permissions.size).apply {
                    fill(false)
                }
            )
        }

        return needToAskPermissions
    }

    private fun onRequestPermissionsResult(
        permissions: Array<String>,
        grantResults: BooleanArray,
        shouldShowRequestPermissionRationale: BooleanArray
    ) {
        var permissionsGranted = true
        var showRationale = false
        val permissionsList = ArrayList<Permission>(permissions.size)
        for (i in permissions.indices) {
            val granted = grantResults[i]
            permissionsGranted = permissionsGranted && granted
            if (!showRationale && !granted && shouldShowRequestPermissionRationale[i]) {
                showRationale = true
            }
            permissionsList.add(Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]))
        }

        when {
            permissionsGranted -> granted.invoke(permissionsList)
            showRationale -> rationale?.invoke(permissionsList)
            else -> denied?.invoke(permissionsList) // never ask again
        }
    }
}