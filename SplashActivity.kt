package com.demomvvm.Register_Login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.demomvvm.*
import java.io.File


@Suppress("DEPRECATION")
@SuppressLint("LongLogTag")
class SplashActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // remove title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)


        //todo for refresh banner - clear all cache start
        try
        {
            applicationContext.cacheDir.deleteRecursively()
            deleteCache(applicationContext)
        }
        catch(ex:Exception)
        {
            Log.i("My TAG", "Error in clear all cache ex = $ex")
        }
        //todo for refresh banner - clear all cache end

        val handler = Handler()
        handler.postDelayed({
            Log.i("My handler ", " called")
            val sharedPreferences = getSharedPreferences(getString (R.string.login_detail), Context.MODE_PRIVATE)
            val login_user_id = sharedPreferences.getString(getString (R.string.usr_id), "").toString()
            if (login_user_id  != "") {
                redirectHomeActivity()
            }
            else{
                redirectLoginActivity()
            }
        },1500)


    }


    fun deleteCache(context: Context) {
        try {
            val dir: File = context.cacheDir
            deleteDir(dir)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory()) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile()) {
            dir.delete()
        } else {
            false
        }
    }




    private fun redirectHomeActivity(){
        Log.i("My redirectHomeActivity ", " called")
        val intent1 = Intent(this, HomeActivity::class.java)
        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent1)
        finish()
        //Animatoo.animateFade(this)
    }
    private fun redirectLoginActivity(){
        Log.i("My redirectLoginActivity ", " called")
        val intent1 = Intent(this, LoginActivity::class.java)
        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent1)
        finish()
        //Animatoo.animateFade(this)
    }
}
