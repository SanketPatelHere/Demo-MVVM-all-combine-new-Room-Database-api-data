package com.demomvvm.Cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.demomvvm.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@SuppressLint("LongLogTag")
public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.CardHolder>{


    private List<CartProductsModel> couponsList;
    private Context context;


    private OnItemClickListener listener = null;

    public interface OnItemClickListener {
        void onItemClick(CartProductsModel item, int possition, String objectItemName, View objView1, TextView objView2);
        //void onItemClick2(CouponsModel item, int possition, String objectItemName, View objView, View objView2);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener getListener() {
        return listener;
    }


    public CartProductsAdapter(Context c, List<CartProductsModel> couponsList, OnItemClickListener listener) {
        this.context = c;
        this.couponsList = couponsList;
        this.listener = listener;
    }


    /*int quantity = 0;
    Type type = new TypeToken<ArrayList<CartProductsModel>>() {}.getType();
    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.cart_detail), Context.MODE_PRIVATE);
    String cart_li = sharedPreferences.getString(context.getString(R.string.cart_list), "");
    Gson gson = new Gson();

    ArrayList<CartProductsModel> cart_list_exist = new ArrayList<CartProductsModel>();
     if(!cart_li.equals(""))
    {
        cart_list_exist = gson.fromJson(cart_li, type);
        int cart_count = 0;
        while (cart_count < cart_list_exist.size()) {
            CartProductsModel shopCategory = cart_list_exist.get(cart_count);
            quantity = quantity + Integer.parseInt(shopCategory.cartProductQuantity);

            cart_count = cart_count +1;
        }
        cart_list_exist.remove(position);
        String json = gson.toJson(cart_list_exist);
        Log.i("My printjson cartProductDelete","finaljson for save in sp = "+json);
        sharedPreferences.edit().putString(context.getString(R.string.cart_list), json).apply();
    }*/



    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_cart_product, parent, false);
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        //coupon_status : 0-redeem   1-yet to be redeem
        CartProductsModel cpm = couponsList.get(position);

        /*
        1-red,
        2-green
        3-black
        4-red
        5-green

         */
        if(cpm.getCartProductImage().equals("1") || cpm.getCartProductImage().equals("4")){
            Picasso.get()
                .load(R.color.MyRed)
                .placeholder(R.color.MyRed)
                .error(R.color.MyRed)
                .into(holder.cartProductImage);
        }
        else if(cpm.getCartProductImage().equals("2") || cpm.getCartProductImage().equals("5")){
            Picasso.get()
                .load(R.color.MyGreen)
                .placeholder(R.color.MyGreen)
                .error(R.color.MyGreen)
                .into(holder.cartProductImage);
        }
        //3
        else{
            Picasso.get()
                .load(R.color.black)
                .placeholder(R.color.black)
                .error(R.color.black)
                .into(holder.cartProductImage);
        }

        holder.cartProductName.setText(cpm.cartProductName);
        holder.cartProductQuantity.setText(cpm.cartProductQuantity);



        holder.cartProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int possition = holder.getAdapterPosition();
                    if(possition != RecyclerView.NO_POSITION){
                        listener.onItemClick(couponsList.get(possition),possition,"cartProductImage",holder.cartProductImage,holder.cartProductQuantity);
                    }
                }
            }
        });


        holder.cartProductMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int possition = holder.getAdapterPosition();
                    if(possition != RecyclerView.NO_POSITION){
                        listener.onItemClick(couponsList.get(possition),possition,"cartProductMinus",holder.cartProductMinus,holder.cartProductQuantity);
                    }
                }
            }
        });

        holder.cartProductQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int possition = holder.getAdapterPosition();
                    if(possition != RecyclerView.NO_POSITION){
                        listener.onItemClick(couponsList.get(possition),possition,"cartProductQuantity",holder.cartProductQuantity,holder.cartProductQuantity);
                    }
                }
            }
        });

        holder.cartProductPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int possition = holder.getAdapterPosition();
                    if(possition != RecyclerView.NO_POSITION){
                        listener.onItemClick(couponsList.get(possition),possition,"cartProductPlus",holder.cartProductPlus,holder.cartProductQuantity);
                    }
                }
            }
        });

        holder.cartProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int possition = holder.getAdapterPosition();
                    if(possition != RecyclerView.NO_POSITION){
                        listener.onItemClick(couponsList.get(possition),possition,"cartProductDelete",holder.cartProductDelete,holder.cartProductQuantity);
                    }
                }
            }
        });



    }


    public class CardHolder extends RecyclerView.ViewHolder {

        public TextView cartProductName,cartProductMinus,cartProductQuantity,cartProductPlus;
        ImageView cartProductImage,cartProductDelete;

        ConstraintLayout mainContainer;

        public CardHolder(@NonNull View view) {
            super(view);

            mainContainer = (ConstraintLayout) view.findViewById(R.id.mainContainer);
            cartProductName = (TextView) view.findViewById(R.id.cartProductName);
            cartProductMinus = (TextView) view.findViewById(R.id.cartProductMinus);
            cartProductQuantity = (TextView) view.findViewById(R.id.cartProductQuantity);
            cartProductPlus = (TextView) view.findViewById(R.id.cartProductPlus);
            cartProductImage = (ImageView) view.findViewById(R.id.cartProductImage);
            cartProductDelete = (ImageView) view.findViewById(R.id.cartProductDelete);


        }
    }


    @Override
    public int getItemCount() {
        return couponsList.size();
    }
}
