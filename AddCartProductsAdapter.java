package com.demomvvm.Cart.Add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demomvvm.Cart.CartProductsModel;
import com.squareup.picasso.Picasso;
import com.demomvvm.R;

import java.util.List;


public class AddCartProductsAdapter extends RecyclerView.Adapter<AddCartProductsAdapter.CardHolder>{


    private List<CartProductsModel> couponsList;
    private Context context;


    private OnItemClickListener listener = null;

    public interface OnItemClickListener {
        void onItemClick(CartProductsModel item, int possition, String objectItemName, View objView1);
        //void onItemClick2(CouponsModel item, int possition, String objectItemName, View objView, View objView2);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener getListener() {
        return listener;
    }


    public AddCartProductsAdapter(Context c, List<CartProductsModel> couponsList, OnItemClickListener listener) {
        this.context = c;
        this.couponsList = couponsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_cart_addproduct, parent, false);
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        //coupon_status : 0-redeem   1-yet to be redeem
        CartProductsModel cpm = couponsList.get(position);

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
        else{
            Picasso.get()
                .load(R.color.black)
                .placeholder(R.color.black)
                .error(R.color.black)
                .into(holder.cartProductImage);
        }

        holder.cartProductName.setText(cpm.getCartProductName());

        holder.cartAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int possition = holder.getAdapterPosition();
                    if(possition != RecyclerView.NO_POSITION){
                        listener.onItemClick(couponsList.get(possition),possition,"cartAddProduct",holder.cartAddProduct);
                    }
                }
            }
        });



    }

    public class CardHolder extends RecyclerView.ViewHolder {

        public TextView cartProductName,cartAddProduct;
        ImageView cartProductImage;


        public CardHolder(@NonNull View view) {
            super(view);

            cartProductName = (TextView) view.findViewById(R.id.cartProductName);
            cartAddProduct = (TextView) view.findViewById(R.id.cartAddProduct);
            cartProductImage = (ImageView) view.findViewById(R.id.cartProductImage);


        }
    }


    @Override
    public int getItemCount() {
        return couponsList.size();
    }
}
