package com.demomvvm.Cart;

public class CartProductsModel {

    String cartProductID;
    String cartProductImage;
    String cartProductName;
    String cartProductQuantity;

    public CartProductsModel(){

    }

    public CartProductsModel(String cartProductID, String cartProductImage, String cartProductName, String cartProductQuantity) {
        this.cartProductID = cartProductID;
        this.cartProductImage = cartProductImage;
        this.cartProductName = cartProductName;
        this.cartProductQuantity = cartProductQuantity;
    }

    public String getCartProductID() {
        return cartProductID;
    }

    public void setCartProductID(String cartProductID) {
        this.cartProductID = cartProductID;
    }

    public String getCartProductImage() {
        return cartProductImage;
    }

    public void setCartProductImage(String cartProductImage) {
        this.cartProductImage = cartProductImage;
    }

    public String getCartProductName() {
        return cartProductName;
    }

    public void setCartProductName(String cartProductName) {
        this.cartProductName = cartProductName;
    }

    public String getCartProductQuantity() {
        return cartProductQuantity;
    }

    public void setCartProductQuantity(String cartProductQuantity) {
        this.cartProductQuantity = cartProductQuantity;
    }
}
