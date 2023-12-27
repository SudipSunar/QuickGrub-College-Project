package com.example.quickgrub.DataModell;

public class UserModel {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;

    public UserModel(){

    }
    public UserModel(String name,String email,String password){
        this.name=name;
        this.email=email;
        this.password=password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
