package com.example.ecommerce_kotlin.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity : AppCompatActivity() {

    var progressDialog: ProgressDialog? = null

    fun showProgressDialog(massage: String?) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(massage)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    open fun showMessage(message: String?, posActionName: String?): AlertDialog? {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(posActionName) { dialog, which -> dialog.dismiss() }
        return builder.show()
    }

    open fun showMessage(message: String?, posActionName: String?,
                         onClickListener: DialogInterface.OnClickListener?,
                         isCancelable: Boolean): androidx.appcompat.app.AlertDialog? {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(posActionName, onClickListener)
        builder.setCancelable(isCancelable)
        return builder.show()
    }

}