package com.example.quickgrub.DataModell;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetails implements Serializable {
    private String name;
    private String address;
    private String phone;
    private String totalAmount;
    private ArrayList<String> foodItemName;
    private ArrayList<String> foodItemPrice;
    private ArrayList<String> foodItemImage;

    private boolean orderAccepted=false;
    private boolean paymentReceived=false;
    private ArrayList<Integer> itemQuantities;
    private String userId;
    private String itemPushKey;
    private Long currentTime;

    public OrderDetails() {
        // Default constructor with no arguments
    }


    public OrderDetails(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        totalAmount = in.readString();
        foodItemName = in.createStringArrayList();
        foodItemPrice = in.createStringArrayList();
        foodItemImage = in.createStringArrayList();

        orderAccepted = in.readByte() != 0;
        paymentReceived = in.readByte() != 0;
        userId = in.readString();
        itemPushKey = in.readString();
        if (in.readByte() == 0) {
            currentTime = null;
        } else {
            currentTime = in.readLong();
        }
    }

    public static final Parcelable.Creator<OrderDetails> CREATOR = new Parcelable.Creator<OrderDetails>() {

        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }


        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };

    public OrderDetails(String userId,
                        String name,
                        String address,
                        String phone,
                        ArrayList<String> foodItemName,
                        ArrayList<String> foodItemPrice,
                        ArrayList<Integer> itemQuantitiesList,
                        ArrayList<String> foodItemImage,
                        String totalAmount,
                        Long time,
                        String itemPushKey,
                        boolean b,
                        boolean b1) {
        this.userId=userId;
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.foodItemName=foodItemName;
        this.foodItemImage=foodItemImage;
        this.foodItemPrice=foodItemPrice;
        this.itemQuantities=itemQuantitiesList;
        this.currentTime=time;
        this.itemPushKey=itemPushKey;
        this.totalAmount=totalAmount;
        this.orderAccepted=b;
        this.paymentReceived=b1;

    }

    public int describeContents() {
        return 0;
    }


    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(totalAmount);
        parcel.writeStringList(foodItemName);
        parcel.writeStringList(foodItemPrice);
        parcel.writeStringList(foodItemImage);
        parcel.writeByte((byte) (orderAccepted ? 1 : 0));
        parcel.writeByte((byte) (paymentReceived ? 1 : 0));
        parcel.writeString(userId);
        parcel.writeString(itemPushKey);
        if (currentTime == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(currentTime);
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Repeat the above pattern for other properties...

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public ArrayList<String> getFoodItemImage() {
        return foodItemImage;
    }

    public void setFoodItemImage(ArrayList<String> foodItemImage) {
        this.foodItemImage = foodItemImage;
    }

    public ArrayList<String> getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(ArrayList<String> foodItemName) {
        this.foodItemName = foodItemName;
    }

    public ArrayList<String> getFoodItemPrice() {
        return foodItemPrice;
    }

    public void setFoodItemPrice(ArrayList<String> foodItemPrice) {
        this.foodItemPrice = foodItemPrice;
    }

    public ArrayList<Integer> getItemQuantities() {
        return itemQuantities;
    }

    public void setItemQuantities(ArrayList<Integer> itemQuantities) {
        this.itemQuantities = itemQuantities;
    }

    public String getItemPushKey() {
        return itemPushKey;
    }

    public void setItemPushKey(String itemPushKey) {
        this.itemPushKey = itemPushKey;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderAccepted(boolean orderAccepted) {
        this.orderAccepted = orderAccepted;
    }

    public void setPaymentReceived(boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }
    public Boolean getPaymentReceived(){
        return paymentReceived;
    }
    public Boolean getOrderAccepted(){
        return orderAccepted;
    }

}
