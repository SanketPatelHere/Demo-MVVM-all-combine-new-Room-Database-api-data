package com.demomvvm.Cart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.demomvvm.R
import com.demomvvm.Singleton
import com.demomvvm.databinding.ActivityViewCartProductsBinding
import java.util.ArrayList

@SuppressLint("LongLogTag","NotifyDataSetChanged")
class ViewCartProductsActivity : AppCompatActivity() {
    
    private var viewCartProduct_rv: RecyclerView? = null
    var CartProductsModelListData: ArrayList<CartProductsModel?> = ArrayList<CartProductsModel?>()
    lateinit var cart_list_exist: ArrayList<CartProductsModel>
    
    private lateinit var binding: ActivityViewCartProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCartProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("My ViewCartProductsActivity by default Singleton #48cfe0 "," Singleton getSite_color = "+ Singleton.getInstance().site_color)
        Log.i("My ViewCartProductsActivity by default Singleton #042638 "," Singleton getSite_bgcolor = "+ Singleton.getInstance().site_bgcolor)
        window.statusBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))
        window.navigationBarColor = Color.parseColor(Singleton.getInstance().site_color.replace(" ", ""))

        binding.backBtn.setOnClickListener {
            finish()
        }

        getCartItems("NO",0)

    }

    fun getCartItems(yesNo :String, int: Int){
        viewCartProduct_rv = findViewById<View>(R.id.viewCartProduct_rv) as RecyclerView
        viewCartProduct_rv?.setHasFixedSize(true)
        viewCartProduct_rv?.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        val recyclerViewState = viewCartProduct_rv?.layoutManager?.onSaveInstanceState()
        viewCartProduct_rv?.adapter?.notifyDataSetChanged()
        viewCartProduct_rv?.layoutManager?.onRestoreInstanceState(recyclerViewState)

        var quantity = 0
        val sharedPreferences = this.getSharedPreferences(getString(R.string.cart_detail), Context.MODE_PRIVATE)
        val cart_li = sharedPreferences?.getString(getString(R.string.cart_list), "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<CartProductsModel>>() {}.type
        if(cart_li != "" )
        {
            cart_list_exist = gson.fromJson(cart_li, type) as ArrayList<CartProductsModel>
            var cart_count = 0
            //when atleast 1 items in cart = 0<1
            while (cart_count < cart_list_exist.size) {
                val shopCategory: CartProductsModel = cart_list_exist.get(cart_count) //sp saved cart list
                Log.i("My if ","shopCategory.cartProductID = "+shopCategory.cartProductID)

                quantity = quantity + shopCategory.cartProductQuantity.toInt()

                var roundPojo: CartProductsModel? = CartProductsModel()
                roundPojo=
                    CartProductsModel(shopCategory.cartProductID,shopCategory.cartProductImage,shopCategory.cartProductName,shopCategory.cartProductQuantity)
                CartProductsModelListData.add(roundPojo)
                cart_count++
            }
            Log.i("My CartProductsModelListData.size =",""+CartProductsModelListData.size)

            if(CartProductsModelListData.size!=0){
                binding.cartProductTotalQuantity.text = "Total Quantity : $quantity"

                /*viewCartProduct_rv?.adapter = CartProductsAdapter(applicationContext,CartProductsModelListData)
                viewCartProduct_rv?.adapter?.notifyDataSetChanged()*/
                viewCartProduct_rv?.adapter =
                    CartProductsAdapter(applicationContext,CartProductsModelListData,
                        CartProductsAdapter.OnItemClickListener() { CartProductsModel: CartProductsModel, indexPos: Int, objectName: String, viewObject1: View, cartProductQuantity_tv: TextView ->
                            val shopCategory: CartProductsModel = cart_list_exist.get(indexPos)
                            val sharedPreferences1 = getSharedPreferences(getString(R.string.cart_detail), MODE_PRIVATE)
                            val editor = sharedPreferences1?.edit()
                            // Image of Product
                            if(objectName == "cartProductImage") {
                                Log.i("My cartProductImage ", "cartProductImage = ${CartProductsModel.cartProductImage}  cartProductName = ${CartProductsModel.cartProductName}")
                            }
                            // Minus Product
                            if(objectName == "cartProductMinus") {
                                Log.i("My cartProductMinus ", "cartProductQuantity = ${CartProductsModel.cartProductQuantity}  cartProductName = ${CartProductsModel.cartProductName}")
                                if(shopCategory.cartProductQuantity.toInt() > 0)
                                {
                                    shopCategory.cartProductQuantity = (shopCategory.cartProductQuantity.toInt() - 1).toString()
                                    val json = gson.toJson(cart_list_exist)
                                    Log.i("My printJson cartProductMinus","finalJson for save in sp = $json")
                                    editor?.putString(getString(R.string.cart_list), json)
                                    editor?.apply()
                                    cartProductQuantity_tv.text = shopCategory.cartProductQuantity
                                    updateQuantity()
                                }
                            }
                            // Plus Product
                            if(objectName == "cartProductPlus") {
                                Log.i("My cartProductPlus ", "cartProductQuantity = ${CartProductsModel.cartProductQuantity}  cartProductName = ${CartProductsModel.cartProductName}")
                                shopCategory.cartProductQuantity = (shopCategory.cartProductQuantity.toInt() + 1).toString()
                                val json = gson.toJson(cart_list_exist)
                                Log.i("My printJson cartProductPlus","finalJson for save in sp = $json")
                                editor?.putString(getString(R.string.cart_list), json)
                                editor?.apply()
                                cartProductQuantity_tv.text = shopCategory.cartProductQuantity
                                updateQuantity()
                            }
                            // Delete Product
                            if(objectName == "cartProductDelete") {
                                Log.i("My cartProductDelete ", "cartProductID = ${CartProductsModel.cartProductID}  cartProductName = ${CartProductsModel.cartProductName}")
                                cart_list_exist.removeAt(indexPos)
                                val json = gson.toJson(cart_list_exist)
                                Log.i("My printJson cartProductDelete","finalJson for save in sp = $json")
                                editor?.putString(getString(R.string.cart_list), json)
                                editor?.apply()
                                CartProductsModelListData.clear()

                                if(indexPos == 0){
                                    getCartItems("NO",0) //for scroll at 0 position
                                }
                                else{
                                    getCartItems("YES",(indexPos-1)) //for scroll at selected's previous position
                                }
                                updateQuantity()
                            }
                        }
                    )
            }

            if(yesNo == "YES"){
                viewCartProduct_rv?.smoothScrollToPosition(int)
            }

        }
    }


    fun updateQuantity(){
        var quantity = 0
        val sharedPreferences = this.getSharedPreferences(getString(R.string.cart_detail), Context.MODE_PRIVATE)
        val cart_li = sharedPreferences?.getString(getString(R.string.cart_list), "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<CartProductsModel>>() {}.type
        if(cart_li != "" )
        {
            cart_list_exist = gson.fromJson(cart_li, type) as ArrayList<CartProductsModel>
            var cart_count = 0
            //when atleast 1 items in cart = 0<1
            while(cart_count < cart_list_exist.size) {
                val shopCategory: CartProductsModel = cart_list_exist.get(cart_count) //sp saved cart list
                quantity = quantity + shopCategory.cartProductQuantity.toInt()
                cart_count++
            }
        }
        binding.cartProductTotalQuantity.text = "Total Quantity : $quantity"
    }


}