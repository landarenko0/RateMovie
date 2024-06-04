package com.example.ratemovie.presentation.loader

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.ratemovie.R

class LoaderDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.apply {
                setView(R.layout.loader_dialog)
                isCancelable = false
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(DIALOG_WIDTH, LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private const val DIALOG_WIDTH = 225
    }
}