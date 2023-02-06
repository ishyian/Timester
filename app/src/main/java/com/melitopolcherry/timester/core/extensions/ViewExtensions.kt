package com.melitopolcherry.timester.core.extensions

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.melitopolcherry.timester.R

fun Context.showSelectorDialog(
    title: String,
    items: List<String>,
    listener: (DialogInterface, Int) -> Unit
) {
    val dialog = AlertDialog.Builder(this)
    dialog.setTitle(title)
    dialog.setItems(items.toTypedArray(), listener)
    dialog.setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
        dialogInterface.dismiss()
    }
    dialog.show()
}

fun Fragment.showSelectorDialog(
    title: String,
    items: List<String>,
    listener: (DialogInterface, Int) -> Unit
) {
    requireContext().showSelectorDialog(title, items, listener)
}