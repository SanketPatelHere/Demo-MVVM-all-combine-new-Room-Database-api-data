package com.demomvvm.Register_Login;

import androidx.annotation.Keep;

@Keep //main when use this - then no need to write @SerializedName("ecard_id") one by one variable
public class UserPojo {
    public String user_id;
    public String username;
    public String userpassword;
    public String useremail;
    public String usermobile;
    public String whatsappnumber;
    public String companyname;
    public String date;
    public String usr_lastlogin;
    public String ip;
    public String profileimage;
    public String address;
    public String status;
    public String subscribe;
    public String usertype;
    public String area;
    public String country;
    public String state;
    public String city;
    public String pincode;
    public String gstnumber;
    public String password2;
    public String registerby;

    public UserPojo()
    {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getWhatsappnumber() {
        return whatsappnumber;
    }

    public void setWhatsappnumber(String whatsappnumber) {
        this.whatsappnumber = whatsappnumber;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsr_lastlogin() {
        return usr_lastlogin;
    }

    public void setUsr_lastlogin(String usr_lastlogin) {
        this.usr_lastlogin = usr_lastlogin;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGstnumber() {
        return gstnumber;
    }

    public void setGstnumber(String gstnumber) {
        this.gstnumber = gstnumber;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getRegisterby() {
        return registerby;
    }

    public void setRegisterby(String registerby) {
        this.registerby = registerby;
    }
}
