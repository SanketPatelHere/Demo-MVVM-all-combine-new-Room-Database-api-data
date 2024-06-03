package com.demomvvm

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.xoxoer.lifemarklibrary.Lifemark


@SuppressLint("LongLogTag", "SetTextI18n","LogNotTimber")
class InAppUpdate(activity: Activity) : InstallStateUpdatedListener {

    private var appUpdateManager: AppUpdateManager
    private val MY_REQUEST_CODE = 500
    private var parentActivity: Activity = activity

    private var currentType = AppUpdateType.FLEXIBLE

    init {
        appUpdateManager = AppUpdateManagerFactory.create(parentActivity)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            // Check if update is available
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // UPDATE IS AVAILABLE
                Log.i("My InAppUpdate ", "UPDATE IS AVAILABLE")
                updateDialog()
            }
            else {
                // UPDATE IS NOT AVAILABLE
                Log.i("My InAppUpdate ", "UPDATE IS NOT AVAILABLE")

            }
        }
        appUpdateManager.registerListener(this)
    }


    private fun startUpdate(info: AppUpdateInfo, type: Int) {
        appUpdateManager.startUpdateFlowForResult(info, type, parentActivity, MY_REQUEST_CODE)
        currentType = type
    }

    fun onResume() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            // Check if update is available
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // UPDATE IS AVAILABLE
                Log.i("My InAppUpdate ", "UPDATE IS AVAILABLE")
                updateDialog()
            }
            else {
                // UPDATE IS NOT AVAILABLE
                Log.i("My InAppUpdate ", "UPDATE IS NOT AVAILABLE")

            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                // If the update is cancelled or fails, you can request to start the update again.
                Log.i("My InAppUpdate ERROR", "Update flow failed! Result Code: $resultCode, Request Code: $requestCode, data: $data")
            }
        }
    }

    private fun flexibleUpdateDownloadCompleted() {
        Snackbar.make(
            parentActivity.findViewById(R.id.contentHomeContainer),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(Color.WHITE)
            show()
        }
    }


    private fun updateDialog() {
        val networkConnection = Lifemark(parentActivity)
        if(networkConnection.isNetworkConnected()){
            Log.i("My InAppUpdate Internet Connection = ", "Available")

            val dialog1: AlertDialog?
            val builder = AlertDialog.Builder(parentActivity)
            //for set the custom layout
            val view = parentActivity.layoutInflater.inflate(R.layout.layout_appupdate_dialog, null)
            //for Get Views references from your layout
            val tvTitle: TextView = view.findViewById(R.id.tv_title)
            val tvDetail: TextView = view.findViewById(R.id.tv_detail)
            val btnUpdate: Button = view.findViewById(R.id.btnUpdate)

            tvTitle.setText(R.string.app_name)
            tvDetail.text = ("Check out the latest version available of "
                    + parentActivity.applicationContext.getString(R.string.app_name) + " app!")

            btnUpdate.setOnClickListener{
                //dialog1?.dismiss()
                val appPackageName = parentActivity.packageName // getPackageName() from Context or Activity object
                Log.i("My InAppUpdate ", "openIntent link = https://play.google.com/store/apps/details?id=$appPackageName")
                parentActivity.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
                ) //show open button instead of update

            }

            builder.setView(view)
            //for create and show the custom alert dialog
            dialog1 = builder.create()
            dialog1.setCancelable(false)
            dialog1.show()
        }
        else
        {
            Log.i("My InAppUpdate Internet Connection = ", "Not Available")
        }


    }

    fun onDestroy() {
        appUpdateManager.unregisterListener(this)
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            flexibleUpdateDownloadCompleted()
        }
    }

}