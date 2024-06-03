package com.demomvvm.Register_Login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.demomvvm.HomeActivity
import com.demomvvm.R
import com.demomvvm.Singleton
import com.demomvvm.databinding.ActivityLoginBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
/*import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider*/
import es.dmoral.toasty.Toasty
import okhttp3.*
import org.json.JSONObject
import java.util.ArrayList
import java.util.Arrays
import java.util.Timer
import kotlin.concurrent.schedule


@Suppress("DEPRECATION")
@SuppressLint("LongLogTag", "LogNotTimber","UseCompatLoadingForDrawables")
class LoginActivity : AppCompatActivity() {


    private var show_password = "NO"


    /*lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    private lateinit var firebaseAuth: FirebaseAuth*/


    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Singleton.getInstance().site_color = "#48cfe0"
        Singleton.getInstance().site_bgcolor = "#042638"
        window.statusBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))
        window.navigationBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))
        val drawable: GradientDrawable = resources.getDrawable(R.drawable.login_button) as GradientDrawable
        val color = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))
        drawable.setColor(color)
        binding.submit.setBackgroundDrawable(drawable)

        binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.showPasswordButton.setImageResource(R.drawable.login_show_icon)

        binding.showPasswordButton.setOnClickListener {
            Log.i("My LoginActivity show_password = ",""+show_password)
            if(show_password == "NO"){
                show_password = "YES" //inputType="textPassword = hide = normal text show
                binding.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.password.setSelection(binding.password.text.length)
                binding.showPasswordButton.setImageResource(R.drawable.login_hide_icon)

            }
            else {
                show_password = "NO"
                binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.password.setSelection(binding.password.text.length)
                binding.showPasswordButton.setImageResource(R.drawable.login_show_icon)

            }
        }
        binding.email.setText("citadelind2@gmail.com")
        binding.password.setText("Dhaval@123")


        binding.submit.setOnClickListener {
            attemptLogin()
        }



        /*FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("963037050539-m9r5bsp02fbnno9g49pbr926v4u92mnq.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        firebaseAuth= FirebaseAuth.getInstance()


        binding.loginWithGoogle.setOnClickListener {
            attemptLogin2()
        }*/






    }

    fun isEmailValid(str: String?): Boolean { //System.out.println("Getting the email");
        return !TextUtils.isEmpty(str) && android.util.Patterns.EMAIL_ADDRESS.matcher(str.toString()).matches()
    }


    private fun attemptLogin() {
        val emailStr=binding.email.text.toString()
        val passwordStr=binding.password.text.toString()

        if(TextUtils.isEmpty(emailStr)){
            binding.email.requestFocus()
            Toasty.error(this, getString(R.string.error_email_required), Toast.LENGTH_SHORT, true).show()
        }
        else if(!isEmailValid(emailStr)){
            binding.email.requestFocus()
            Toasty.error(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT, true).show()
        }
        else if (TextUtils.isEmpty(passwordStr) ) {
            binding.password.requestFocus()
            Toasty.error(this, getString(R.string.error_password_required), Toast.LENGTH_SHORT, true).show()
        }
        else{
            binding.loginActivityProgressBar.visibility= View.VISIBLE

            val api_url = getString(R.string.api_base_url) + "/localshopadmin/lslogin"
            val authstr = getString(R.string.api_auth_user) + ":" + getString(R.string.api_auth_pass)
            val encodedString  = android.util.Base64.encodeToString(
                authstr.toByteArray(),
                android.util.Base64.NO_WRAP
            )
            val encoding = encodedString as String
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .authenticator(object : Authenticator {
                    @Throws(Exception::class)
                    override fun authenticate(route: Route?, response: Response): Request {
                        return response.request.newBuilder()
                            .header("Authorization", "Basic $encoding")
                            .build()
                    }
                })
                .build()

            Log.i("My LoginActivity lslogin API params emailStr =",""+emailStr)
            Log.i("My LoginActivity lslogin API params passwordStr =",""+passwordStr)
            AndroidNetworking.post(api_url)
                .addBodyParameter("email",emailStr) //citadelind2@gmail.com
                .addBodyParameter("password",passwordStr) //Dhaval@123
                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.i("My LoginActivity lslogin API Response =",""+response.toString())
                        if (response.getString("status") == "200") {
                            val data = response.getJSONArray("data")

                            var localusr_id=""
                            var site_id=""
                            var reg_id=""
                            var agent_id=""
                            var localusr_firstname=""
                            var localusr_lastname=""
                            var localusr_email=""
                            var localusr_businessname=""
                            var localusr_username=""
                            var localusr_password=""
                            var localusr_phone=""
                            var localusr_address_b=""
                            var localusr_address_s=""
                            var localusr_city=""
                            var localusr_state=""
                            var localusr_zipcode=""
                            var localusr_country=""
                            var localusr_member=""
                            var localusr_rdate=""
                            var localusr_lastlogin=""
                            var ip=""
                            var transaction_id=""
                            var localusr_bdate=""
                            var localusr_gender=""
                            var agent_comment=""
                            var agent_rating=""
                            var localusr_status=""
                            var localusr_zone=""
                            var localusr_region=""

                            /*
                            //for direct save whole object in pojo
                            //no need to write 1-1 field in while loop
                            //Object=>Array=>ArrayList<Pojo>
                            //JsonObject=>JsonArray=>ArrayList<CharityPojo>
                            val gson = GsonBuilder().serializeNulls().create() //return null => "" //not work
                            val jsonObject: JsonObject = gson.fromJson(response.toString(), JsonObject::class.java) //whole api res
                            i(TAG, "myUserList getUserInfo jsonObject = "+jsonObject)
                            val dataObjectResponse: JsonArray = jsonObject.getAsJsonArray("data") //only main data res in JsonArray
                            i(TAG, "myUserList getUserInfo dataArrayResponse = "+dataObjectResponse)
                            //val responseDataVales: MutableList<CharityPojo> = gson.fromJson(dataObjectResponse, object: TypeToken<List<CharityPojo>>(){}.type) //save whole main data array in pojo array
                            val responseDataVales: ArrayList<UserPojo> = gson.fromJson(dataObjectResponse, object: TypeToken<List<UserPojo>>(){}.type) //save whole main data array in pojo array, TypeToken=Represents a generic type T(when pojo class type not provide, it arrive at runttime)
                            ///i(TAG, "getAllCharityData responseDataVales = "+responseDataVales) //only main data res in pojo array
                            i(TAG, "myUserList getUserInfo responseDataVales = "+ Arrays.toString(responseDataVales.toArray())) //only main data res in pojo array


                            //if(responseDataVales.size>0)
                            if(responseDataVales!=null && responseDataVales.size>0) //[] or [com.wemakeadifference.Charity.CharityPojo@f4fca2f, com.wemakeadifference.Charity.CharityPojo@3ed983c]
                            {
                                //data array exist
                                Log.i(TAG, "myUserList getUserInfo data array exist")
                                ///my_charity_list.add(responseDataVales) //not working
                                myUserList = responseDataVales //working
                                var userStatus = myUserList.get(0).status
                                Log.i(TAG, "myUserList getUserInfo userStatus = "+userStatus)

                                //todo for update all new value from api, when some data direct change from mysql or other device like admin phone start
                                val usertype = myUserList.get(0).usertype
                                val username = myUserList.get(0).username
                                Log.i(TAG, "myUserList getUserInfo username = "+username)
                                val useremail = myUserList.get(0).useremail
                                val usermobile = myUserList.get(0).usermobile
                                val whatsappnumber = myUserList.get(0).whatsappnumber
                                val companyname = myUserList.get(0).companyname //shopname
                                val shopname = myUserList.get(0).companyname //shopname
                                val gstnumber = myUserList.get(0).gstnumber
                                val area = myUserList.get(0).area
                                val address = myUserList.get(0).address
                                val city = myUserList.get(0).city
                                val state = myUserList.get(0).state
                                val country = myUserList.get(0).country
                                val pincode = myUserList.get(0).pincode
                                val registerby = myUserList.get(0).registerby

                                //for saved register res for php
                                val sharedPreferences1 = applicationContext.getSharedPreferences(getString(
                                    R.string.login_detail), Context.MODE_PRIVATE)
                                val editor = sharedPreferences1.edit()
                                editor.putString(getString(R.string.userid),user_id) //php user_id(use this in whole project)
                                editor?.putString(getString(R.string.usertype), usertype)
                                editor?.putString(getString(R.string.name), username)
                                editor?.putString(getString(R.string.shopname), shopname)
                                editor?.putString(getString(R.string.email), useremail)
                                editor?.putString(getString(R.string.phone), usermobile)
                                editor?.putString(getString(R.string.whatsappnumber), whatsappnumber)
                                editor?.putString(getString(R.string.gstnumber), gstnumber)
                                editor?.putString(getString(R.string.area),area)
                                editor?.putString(getString(R.string.address),address)
                                editor?.putString(getString(R.string.city), city)
                                editor?.putString(getString(R.string.state), state)
                                editor?.putString(getString(R.string.country), country)
                                editor?.putString(getString(R.string.pincode), pincode)
                                editor?.putString(getString(R.string.registerby), registerby)
                                editor.apply()

                                //for saved register res for firebase
                                registerInFirebase()

                                //todo for update all new value from api, when some data direct change from mysql or other device like admin phone end


                                
                            }
                            */


                            for (j in 0 until data.length()) {
                                val data_dic = data.getJSONObject(j)

                                if (data_dic.has("localusr_id")) {
                                    localusr_id = data_dic.getString("localusr_id")
                                }
                                if (data_dic.has("site_id")) {
                                    site_id = data_dic.getString("site_id")
                                }
                                if (data_dic.has("reg_id")) {
                                    reg_id = data_dic.getString("reg_id")
                                }
                                if (data_dic.has("agent_id")) {
                                    agent_id = data_dic.getString("agent_id")
                                }
                                if (data_dic.has("localusr_firstname")) {
                                    localusr_firstname = data_dic.getString("localusr_firstname")
                                }
                                if (data_dic.has("localusr_lastname")) {
                                    localusr_lastname = data_dic.getString("localusr_lastname")
                                }
                                if (data_dic.has("localusr_email")) {
                                    localusr_email = data_dic.getString("localusr_email")
                                }
                                if (data_dic.has("localusr_phone")) {
                                    localusr_phone = data_dic.getString("localusr_phone")
                                }
                                if (data_dic.has("localusr_address_s")) {
                                    localusr_address_s = data_dic.getString("localusr_address_s")
                                }
                                if (data_dic.has("localusr_city")) {
                                    localusr_city = data_dic.getString("localusr_city")
                                }
                                if (data_dic.has("localusr_state")) {
                                    localusr_state = data_dic.getString("localusr_state")
                                }
                                if (data_dic.has("localusr_zipcode")) {
                                    localusr_zipcode = data_dic.getString("localusr_zipcode")
                                }
                                if (data_dic.has("localusr_country")) {
                                    localusr_country = data_dic.getString("localusr_country")
                                }
                                if (data_dic.has("localusr_bdate")) {
                                    localusr_bdate = data_dic.getString("localusr_bdate")
                                }
                                if (data_dic.has("localusr_gender")) {
                                    localusr_gender = data_dic.getString("localusr_gender")
                                }
                                if (data_dic.has("localusr_zone")) {
                                    localusr_zone = data_dic.getString("localusr_zone")
                                }
                                if (data_dic.has("localusr_region")) {
                                    localusr_region = data_dic.getString("localusr_region")
                                }

                            }
                            val api_url2 = getString(R.string.api_base_url) + "/localshopadmin/lshomelayout"
                            AndroidNetworking.post(api_url2)
                                .addBodyParameter("site_id",site_id)
                                .addBodyParameter("user_id",localusr_id)
                                .setOkHttpClient(okHttpClient)
                                .setTag("test")
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener {
                                    override fun onResponse(response: JSONObject) {
                                        binding.loginActivityProgressBar.visibility = View.INVISIBLE
                                        Log.i("My LoginActivity lshomelayout API Response =", "" + response.toString())
                                        if (response.getString("status") == "200") {
                                            val data2 = response.getJSONObject("sitedata")//{}
                                            //val data = response.getJSONArray("sitedata");//[]

                                            var SITE_TITLE = ""
                                            var SITE_COLOR = ""
                                            var SITE_BGCOLOR = ""
                                            var localshop_id = ""
                                            var localshop_title = ""
                                            var localshop_image = ""
                                            if (data2 != null){
                                                SITE_TITLE = data2.getString("SITE_TITLE")
                                                SITE_COLOR = data2.getString("SITE_COLOR")
                                                SITE_BGCOLOR = data2.getString("SITE_BGCOLOR")

                                                Singleton.getInstance().site_color = SITE_COLOR
                                                Singleton.getInstance().site_bgcolor = SITE_BGCOLOR

                                                Log.i("My LoginActivity API by default Singleton #48cfe0 "," Singleton getSite_color = "+ Singleton.getInstance().site_color)
                                                Log.i("My LoginActivity API by default Singleton #042638 "," Singleton getSite_bgcolor = "+ Singleton.getInstance().site_bgcolor)
                                                val data3 = response.getJSONArray("userinfo")
                                                for (j in 0 until data3.length()) {
                                                    val data_dic = data3.getJSONObject(j)

                                                    if (data_dic.has("localshop_id")) {
                                                        localshop_id = data_dic.getString("localshop_id")
                                                    }
                                                    if (data_dic.has("localshop_title")) {
                                                        localshop_title = data_dic.getString("localshop_title")
                                                    }
                                                    if (data_dic.has("localshop_image")) {
                                                        localshop_image = data_dic.getString("localshop_image")
                                                    }

                                                }


                                                val sharedPreferences = getSharedPreferences(getString (R.string.login_detail), Context.MODE_PRIVATE)
                                                val editor = sharedPreferences.edit()
                                                editor.putString("icon","1")
                                                editor.putString(getString (R.string.usr_id),localusr_id )
                                                editor.putString(getString (R.string.site_id),site_id)
                                                editor.putString(getString (R.string.reg_id),reg_id)
                                                editor.putString(getString (R.string.agent_id),agent_id)
                                                editor.putString(getString (R.string.usr_firstname), localusr_firstname)
                                                editor.putString(getString (R.string.usr_lastname), localusr_lastname)
                                                editor.putString(getString (R.string.localusr_email), localusr_email)
                                                editor.putString(getString (R.string.localusr_phone), localusr_phone)
                                                editor.putString(getString (R.string.localusr_address_s), localusr_address_s)
                                                editor.putString(getString (R.string.localusr_city), localusr_city)
                                                editor.putString(getString (R.string.localusr_state), localusr_state)
                                                editor.putString(getString (R.string.localusr_zipcode), localusr_zipcode)
                                                editor.putString(getString (R.string.localusr_country), localusr_country)
                                                editor.putString(getString (R.string.localusr_bdate), localusr_bdate)
                                                editor.putString(getString (R.string.localusr_gender), localusr_gender)
                                                editor.putString(getString (R.string.localusr_zone), localusr_zone)
                                                editor.putString(getString (R.string.localusr_region), localusr_region)
                                                editor.putString(getString (R.string.SITE_TITLE), SITE_TITLE)
                                                editor.putString(getString (R.string.SITE_COLOR), SITE_COLOR)
                                                editor.putString(getString (R.string.SITE_BGCOLOR), SITE_BGCOLOR)
                                                editor.putString(getString (R.string.localshop_id), localshop_id)
                                                editor.putString(getString (R.string.localshop_title), localshop_title)
                                                editor.putString(getString (R.string.localshop_image), localshop_image)
                                                editor.apply()

                                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                                startActivity(intent)
                                                finish()

                                            }


                                        }
                                    }
                                    override fun onError(anError: ANError?) {
                                        Log.i("My LoginActivity lshomelayout API anError =", "" + anError.toString())
                                        binding.loginActivityProgressBar.visibility = View.INVISIBLE
                                    }

                                })

                        }
                        else{
                            binding.loginActivityProgressBar.visibility = View.INVISIBLE
                            Toasty.error(this@LoginActivity, "Wrong Username or Password!", Toast.LENGTH_SHORT, true).show()
                        }
                    }
                    override fun onError(error: ANError) {
                        binding.loginActivityProgressBar.visibility= View.INVISIBLE
                        Log.i("My LoginActivity2 lslogin API Error =",""+error)
                    }
                })
        }

    }




    /*fun attemptLogin2(){
        signInGoogle()
    }


    // signInGoogle() function
    private  fun signInGoogle(){
        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
//            firebaseAuthWithGoogle(account!!)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                Log.i("My account.email.toString() = ",""+account.email.toString())
                Log.i("My account.displayName.toString() = ",""+account.displayName.toString())
                Log.i("My account.photoUrl.toString() = ",""+account.photoUrl.toString())
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }*/





}