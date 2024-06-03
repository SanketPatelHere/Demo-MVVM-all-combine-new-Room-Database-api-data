package com.demomvvm


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.demomvvm.Cart.AddCartProductsActivity
import com.demomvvm.LoadMoreRecycleView.LoadMoreRecycleViewActivity
import com.demomvvm.MVVM.MainActivity
import com.demomvvm.Register_Login.LoginActivity
import com.demomvvm.Retrofit.NewRetrofitActivity
import com.demomvvm.Retrofit.NewRetrofitActivity2
import com.demomvvm.SearchFilter.AllProductsSearch.SearchProductsActivity
import com.demomvvm.Volley.MyVolleyActivity
import com.demomvvm.databinding.ActivityHomeDrawerBinding
import com.demomvvm.databinding.NavHeaderHomeBinding
import okhttp3.*
import java.util.*


@Suppress("DEPRECATION")
@SuppressLint("LongLogTag","LogNotTimber","UseCompatLoadingForDrawables", "SetTextI18n")
class HomeActivity : AppCompatActivity(){

    private var isMemberVisible=false

    private lateinit var binding: ActivityHomeDrawerBinding
    @SuppressLint("Recycle","ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)


        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        binding.appVersion.text = "Version : "+info.versionName

        val sharedPreferences = this.getSharedPreferences(getString(R.string.login_detail), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        //Change Icon
        val icon = sharedPreferences?.getString("icon", "").toString()
        binding.contentHome.localShopLogo.setOnClickListener {
            binding.contentHome.iconThemeContainer.visibility=View.VISIBLE
        }
        binding.contentHome.closeIconThemeContainer.setOnClickListener {
            binding.contentHome.iconThemeContainer.visibility=View.INVISIBLE
        }





        //for change whole app color base on app icon select
        if(icon=="1"){
            binding.contentHome.icon1Background.setBackgroundColor(Color.parseColor("#9FCDF1"))
            binding.contentHome.icon2Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.icon3Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.localShopLogo.setImageResource(R.drawable.app_icon1)
            Singleton.getInstance().site_color = "#c4ccfc"
        }
        else if(icon=="2"){
            binding.contentHome.icon1Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.icon2Background.setBackgroundColor(Color.parseColor("#9FCDF1"))
            binding.contentHome.icon3Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.localShopLogo.setImageResource(R.drawable.app_icon2)
            Singleton.getInstance().site_color = "#ffc4d4"
        }
        else{
            binding.contentHome.icon1Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.icon2Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.icon3Background.setBackgroundColor(Color.parseColor("#9FCDF1"))
            binding.contentHome.localShopLogo.setImageResource(R.drawable.app_icon3)
            Singleton.getInstance().site_color = "#e42c2c"
        }

        binding.navHeaderHome.navHeaderContainer.setBackgroundColor(Color.parseColor(Singleton.getInstance().site_color.replace(" ", "")))
        window.statusBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))
        window.navigationBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))



        //for change app icon color and app close and again open app
        binding.contentHome.icon1Background.setOnClickListener {
            binding.contentHome.icon1Background.setBackgroundColor(Color.parseColor("#9FCDF1"))
            binding.contentHome.icon2Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.icon3Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.localShopLogo.setImageResource(R.drawable.app_icon1)
            editor.putString("icon","1").apply()
            appIcon1()
        }
        binding.contentHome.icon2Background.setOnClickListener {
            binding.contentHome.icon1Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.icon2Background.setBackgroundColor(Color.parseColor("#9FCDF1"))
            binding.contentHome.icon3Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.localShopLogo.setImageResource(R.drawable.app_icon2)
            editor.putString("icon","2").apply()
            appIcon2()
        }
        binding.contentHome.icon3Background.setOnClickListener {
            binding.contentHome.icon1Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.icon2Background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.contentHome.icon3Background.setBackgroundColor(Color.parseColor("#9FCDF1"))
            binding.contentHome.localShopLogo.setImageResource(R.drawable.app_icon3)
            editor.putString("icon","3").apply()
            appIcon3()
        }



        binding.contentHome.logout.setOnClickListener {
            logout()
        }

        binding.contentHome.volley.setOnClickListener {
            val intent1 = Intent(this, MyVolleyActivity::class.java)
            startActivity(intent1)
        }
        binding.contentHome.retrofit.setOnClickListener {
            val intent1 = Intent(this, NewRetrofitActivity::class.java)
            startActivity(intent1)
        }
        binding.contentHome.retrofit2.setOnClickListener {
            val intent1 = Intent(this, NewRetrofitActivity2::class.java)
            startActivity(intent1)
        }
        binding.contentHome.cartActivity.setOnClickListener {
            val intent1 = Intent(this, AddCartProductsActivity::class.java)
            startActivity(intent1)
        }
        binding.contentHome.searchFilter.setOnClickListener {
            val intent1 = Intent(this, SearchProductsActivity::class.java)
            startActivity(intent1)
        }
        binding.contentHome.loadMore.setOnClickListener {
            val intent1 = Intent(this, LoadMoreRecycleViewActivity::class.java)
            startActivity(intent1)
        }
        binding.contentHome.mvvm.setOnClickListener {
            val intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)
        }



        //                          To Access Nav Header
        /*val viewHeader = binding.navView.getHeaderView(0)
        // nav_header.xml is headerLayout
        val navViewHeaderBinding : NavHeaderHomeBinding = NavHeaderHomeBinding.bind(viewHeader)
        // userNameTitle is Children of nav_header
        navViewHeaderBinding.lblUserName.text = "Hi, D!"*/
        binding.navHeaderHome.lblUserName.text = "Hi, DP!"

        //--------------------------Navigation Drawer And Bar
        binding.navView.menu.findItem(R.id.setting_option).setActionView(R.layout.menu_down)
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.setting_option -> {
                    if(!isMemberVisible){
                        binding.navView.menu.findItem(R.id.setting_option).setActionView(R.layout.menu_up)
                        binding.navView.menu.setGroupVisible(R.id.memberBehavior, true)
                        isMemberVisible=true
                    }
                    else{
                        binding.navView.menu.findItem(R.id.setting_option).setActionView(R.layout.menu_down)
                        binding.navView.menu.setGroupVisible(R.id.memberBehavior, false)
                        isMemberVisible=false
                    }
                    true
                }
                else -> false
            }
        }



    }

    private fun appIcon1() {
        Log.i("My --------------------", "appIcon1() called")
        val pm = packageManager
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.splashActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.splashActivity3"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.SplashActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val handler = Handler()
        handler.postDelayed({
            //recreate()
            val intent1 = Intent(this, HomeActivity::class.java)
            startActivity(intent1)
            finish()
        },2700)
        //Toast.makeText(this@HomeActivity, "Enable Old Icon", Toast.LENGTH_LONG).show()
    }
    private fun appIcon2() {
        Log.i("My --------------------", "appIcon2() called")
        val pm = packageManager
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.SplashActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.splashActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.splashActivity3"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        val handler = Handler()
        handler.postDelayed({
            //recreate()
            val intent1 = Intent(this, HomeActivity::class.java)
            startActivity(intent1)
            finish()
        },2700)
        //Toast.makeText(this@HomeActivity, "Enable New Icon", Toast.LENGTH_LONG).show()
    }
    private fun appIcon3() {
        Log.i("My --------------------", "appIcon3() called")
        val pm = packageManager
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.SplashActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.splashActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName("com.demomvvm", "com.demomvvm.Register_Login.splashActivity3"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val handler = Handler()
        handler.postDelayed({
            //recreate()
            val intent1 = Intent(this, HomeActivity::class.java)
            startActivity(intent1)
            finish()
        },2700)
        //Toast.makeText(this@HomeActivity, "Enable New Icon", Toast.LENGTH_LONG).show()
    }



    private fun logout() {
        Log.i("My --------------------", "logoutbutton clicked")
        val sharedPreferences = this.getSharedPreferences(getString(R.string.login_detail), Context.MODE_PRIVATE)
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Do you really want to logout ?")
        alertDialog.setPositiveButton("yes") { _, _ ->
            Log.i("My --------------------", "Yes clicked(logoutbutton)")
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent1 = Intent(this, LoginActivity::class.java)
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent1)
            finish()
        }
        alertDialog.setNegativeButton("No") { _, _ ->
            Log.i("My --------------------", "No clicked(logoutbutton)")
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }






    override fun onDestroy() {
        Log.i("My HomeActivity ","onDestroy called")
        super.onDestroy()
    }

    //For No Internet Connection Dialog
    override fun onResume() {
        Log.i("My HomeActivity ","onResume called")
        super.onResume()
    }


    override fun onBackPressed() {
        Log.i("My HomeActivity ", "onBackPressed()")
        super.onBackPressed()
    }




}