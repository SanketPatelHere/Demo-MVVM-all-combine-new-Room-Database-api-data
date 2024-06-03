package com.demomvvm.Cart

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demomvvm.Cart.Add.AddCartProductsAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.demomvvm.R
import com.demomvvm.Singleton
import com.demomvvm.databinding.ActivityAddCartProductsBinding
import com.demomvvm.Cart.ViewCartProductsActivity
import java.util.ArrayList


@SuppressLint("LongLogTag")
class AddCartProductsActivity : AppCompatActivity() {

    private var addCartProduct_rv: RecyclerView? = null
    var CartProductsModelListData: java.util.ArrayList<CartProductsModel?> = java.util.ArrayList<CartProductsModel?>()
    lateinit var cart_list_exist: ArrayList<CartProductsModel>


    private lateinit var binding: ActivityAddCartProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCartProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("My AddCartProductsActivity by default Singleton #48cfe0 "," Singleton getSite_color = "+ Singleton.getInstance().site_color)
        Log.i("My AddCartProductsActivity by default Singleton #042638 "," Singleton getSite_bgcolor = "+ Singleton.getInstance().site_bgcolor)
        window.statusBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))
        window.navigationBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))

        binding.backBtn.setOnClickListener {
            finish()
        }


        getProducts()

        binding.btnCart1.setOnClickListener {
            val intent1 = Intent(this, ViewCartProductsActivity::class.java)
            startActivity(intent1)
        }

    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = this.getSharedPreferences(getString(R.string.cart_detail), Context.MODE_PRIVATE)
        val cart_li = sharedPreferences?.getString(getString(R.string.cart_list), "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<CartProductsModel>>() {}.type
        Log.i("My AddCartProductsAdapter ","cart_li = ${cart_li}")
        if(cart_li != "" && cart_li != "[]" )
        {
            cart_list_exist = gson.fromJson(cart_li, type) as ArrayList<CartProductsModel>
            var cart_count: Int = 0;
            //when atleast 1 items in cart = 0<1
            while (cart_count < cart_list_exist.size) {
                cart_count++
            }
            binding.badge1.visibility = View.VISIBLE
            binding.badge1.setText(cart_count.toString())

        }
        else{
            binding.badge1.visibility = View.INVISIBLE
        }

    }


    fun getProducts(){

        addCartProduct_rv = findViewById<View>(R.id.addCartProduct_rv) as RecyclerView
        addCartProduct_rv?.setHasFixedSize(true)
        addCartProduct_rv?.setLayoutManager(LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false))
        addCartProduct_rv?.itemAnimator = null

        val recyclerViewState = addCartProduct_rv?.layoutManager?.onSaveInstanceState()
        addCartProduct_rv?.adapter?.notifyDataSetChanged()
        addCartProduct_rv?.layoutManager?.onRestoreInstanceState(recyclerViewState)
        
        for (j in 0 until 6) {
            var roundPojo: CartProductsModel? = CartProductsModel()
            roundPojo=
                CartProductsModel((j+1).toString(),(j+1).toString(),"Product ${(j+1)}","5")
            CartProductsModelListData.add(roundPojo)
        }
        addCartProduct_rv?.adapter =
            AddCartProductsAdapter(applicationContext,CartProductsModelListData,
                AddCartProductsAdapter.OnItemClickListener() { CartProductsModel: CartProductsModel, indexPos: Int, objectName: String, viewObject1: View ->
                    // View Coupon
                    if(objectName == "cartAddProduct"){
                        Log.i("My AddCartProductsAdapter ","cartProductID = ${CartProductsModel.cartProductID}  cartProductName = ${CartProductsModel.cartProductName}")

                        val item_cart = CartProductsModel(
                            CartProductsModel.cartProductID,
                            CartProductsModel.cartProductImage,
                            CartProductsModel.cartProductName,
                            CartProductsModel.cartProductQuantity)

                        val sharedPreferences = this.getSharedPreferences(getString(R.string.cart_detail), MODE_PRIVATE)
                        val cart_li = sharedPreferences?.getString(getString(R.string.cart_list), "")
                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<CartProductsModel>>() {}.type

                        //second item
                        //second time and every time add
                        if(cart_li != "" && cart_li != "[]" )
                        {
                            var check = false
                            cart_list_exist = gson.fromJson(cart_li, type) as ArrayList<CartProductsModel>
                            var cart_count: Int = 0;
                            //when atleast 1 items in cart = 0<1
                            //use loop to check same item already save in list or not
                            while(cart_count < cart_list_exist.size) {
                                val shopCategory: CartProductsModel = cart_list_exist.get(cart_count); //sp saved cart list
                                if(shopCategory.cartProductID == CartProductsModel.cartProductID) //sp and itemCart.item_id
                                {
                                    Log.i("My if ","shopCategory.cartProductID = "+shopCategory.cartProductID)
                                    //second time and every time add(not again save in list, just increase quantity)
                                    shopCategory.cartProductQuantity = (shopCategory.cartProductQuantity.toInt() + CartProductsModel.cartProductQuantity.toInt()).toString() //itemCart.item_quantity
                                    check = true
                                    break
                                }
                                else
                                {
                                    Log.i("My else ","shopCategory.cartProductID = "+shopCategory.cartProductID)
                                }
                                cart_count++
                            }




                            val editor = sharedPreferences?.edit()
                            if(!check)
                            {
                                //new item save in first time add
                                cart_list_exist.add(item_cart)
                            }
                            val json = gson.toJson(cart_list_exist)
                            Log.i("My printjson if","finaljson for save in sp = "+json)
                            //for update new added cart list in sp
                            editor?.putString(getString (R.string.cart_list), json)
                            editor?.apply()
                            showNotificationBagde()
                        }
                        //first item
                        //first time add
                        else{
                            cart_list_exist=ArrayList()
                            //first item save in first time add
                            cart_list_exist.add(item_cart)
                            /// Cart List Save On Prefrence
                            val editor = sharedPreferences.edit()
                            val json = gson.toJson(cart_list_exist)
                            Log.i("My printjson else","finaljson for save in sp = "+json)

                            editor?.putString(getString(R.string.cart_list), json)
                            editor?.apply()
                            showNotificationBagde()
                        }
                    }
                }
            )

    }

    fun showNotificationBagde(){
        val sharedPreferences = this.getSharedPreferences(getString(R.string.cart_detail), Context.MODE_PRIVATE)
        val cart_li = sharedPreferences?.getString(getString(R.string.cart_list), "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<CartProductsModel>>() {}.type
        Log.i("My AddCartProductsAdapter showBagde()","cart_li = ${cart_li}")
        if(cart_li != "" && cart_li != "[]" )
        {
            cart_list_exist = gson.fromJson(cart_li, type) as ArrayList<CartProductsModel>
            var cart_count: Int = 0;
            //when atleast 1 items in cart = 0<1
            while (cart_count < cart_list_exist.size) {
                cart_count++
            }
            binding.badge1.visibility = View.VISIBLE
            binding.badge1.setText(cart_count.toString())

        }
        else{
            binding.badge1.visibility = View.INVISIBLE
        }
    }



}