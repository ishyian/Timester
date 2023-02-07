package com.melitopolcherry.timester.core.contract

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresPermission
import timber.log.Timber

class PickFileActivityContract : ActivityResultContract<Unit, Uri?>() {

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    override fun createIntent(context: Context, input: Unit): Intent {
        return createFileIntent()
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (intent == null || resultCode != Activity.RESULT_OK) {
            Timber.d("photo null")
            return null
        }

        return intent.data
    }
}

@RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
fun createFileIntent() = Intent(Intent.ACTION_GET_CONTENT)
    .apply {
        type = "image/*"
    }