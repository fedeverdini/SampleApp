package com.example.sampleapp.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sampleapp.R
import com.example.sampleapp.model.error.BaseError
import com.example.sampleapp.ui.transaction.list.TransactionListFragment
import com.example.sampleapp.ui.main.ErrorDialogFragment.Companion.ERROR_FRAGMENT_TAG
import com.example.sampleapp.ui.main.LoadingDialogFragment.Companion.LOADING_FRAGMENT_TAG


class MainActivity : AppCompatActivity() {

    private var isLoading = false
    private val loadingDialog = LoadingDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            replaceFragment(TransactionListFragment())
        }
    }

    fun showLoading(value: Boolean) {
        supportFragmentManager.let { fragmentManager ->
            when {
                !isLoading && value -> {
                    loadingDialog.show(fragmentManager, LOADING_FRAGMENT_TAG)
                }
                isLoading && !value -> {
                    loadingDialog.dismiss()
                }
                else -> Unit
            }
            isLoading = value
        }
    }

    fun showError(error: BaseError, listener: ErrorDialogFragment.ErrorListener? = null) {
        val dialog = ErrorDialogFragment.newInstance(error, listener)
        dialog.show(supportFragmentManager, ERROR_FRAGMENT_TAG)
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        if (addToBackStack) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
            supportFragmentManager.executePendingTransactions()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, fragment)
                .commitNow()
        }
    }

    override fun onBackPressed() {
        val fl = findViewById<View>(R.id.mainFragmentContainer) as FrameLayout
        if (fl.childCount == 1) {
            super.onBackPressed()
            if (fl.childCount == 0) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.close_app_title))
                    .setMessage(getString(R.string.close_app_confirmation_message))
                    .setPositiveButton(
                        getString(R.string.yes),
                        DialogInterface.OnClickListener { _, _ -> finish() })
                    .setNegativeButton(
                        getString(R.string.no),
                        DialogInterface.OnClickListener { _, _ ->
                            replaceFragment(TransactionListFragment())
                        })
                    .show()
            }
        } else if (fl.childCount == 0) {
            replaceFragment(TransactionListFragment())
        } else {
            super.onBackPressed()
        }
    }
}