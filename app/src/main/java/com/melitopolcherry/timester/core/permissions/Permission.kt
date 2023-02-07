package com.melitopolcherry.timester.core.permissions

data class Permission @JvmOverloads constructor(
    val name: String?,
    val granted: Boolean,
    val shouldShowRequestPermissionRationale: Boolean = false
)