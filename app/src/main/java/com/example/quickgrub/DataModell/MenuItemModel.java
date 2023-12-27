package com.example.quickgrub.DataModell;

public class MenuItemModel {
    private  String foodName;
    private  String foodPrice;
    private  String foodImage;
    private  String foodDescription;
    private String foodIngredient;

    public MenuItemModel() {
        // require for firebase database
    }
    public MenuItemModel(String foodName, String foodPrice, String foodImage, String foodDescription, String foodIngredient){

        this.foodName=foodName;
        this.foodPrice=foodPrice;
        this.foodImage=foodImage;
        this.foodDescription=foodDescription;
        this.foodIngredient=foodIngredient;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodIngredient() {
        return foodIngredient;
    }

    public void setFoodIngredient(String foodIngredient) {
        this.foodIngredient = foodIngredient;
    }
}
