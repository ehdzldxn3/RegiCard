package com.example.regicard.DATA;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegicardDTO implements Serializable {

    @SerializedName("resno") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String resno;   //객실번호

    @SerializedName("name")
    @Expose
    public String name;    //전화번호

    @SerializedName("arrdt")
    @Expose
    public String arrdt;

    @SerializedName("depdt")
    @Expose
    public String depdt;

    @SerializedName("nights")
    @Expose
    public String nights;

    @SerializedName("roomtype")
    @Expose
    public String roomtype;

    @SerializedName("adult")
    @Expose
    public String adult;

    @SerializedName("child")
    @Expose
    public String child;

    @SerializedName("tel")
    @Expose
    public String tel;

    @SerializedName("passport")
    @Expose
    public String passport;

    @SerializedName("nation")
    @Expose
    public String nation;

    @SerializedName("remark")
    @Expose
    public String remark;

    @SerializedName("Companynm")
    @Expose
    public String Companynm;

    @SerializedName("roomno")
    @Expose
    public String roomno;

    @SerializedName("phone")
    @Expose
    public String phone;    //전화번호

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getResno() {
        return resno;
    }

    public void setResno(String resno) {
        this.resno = resno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArrdt() {
        return arrdt;
    }

    public void setArrdt(String arrdt) {
        this.arrdt = arrdt;
    }

    public String getDepdt() {
        return depdt;
    }

    public void setDepdt(String depdt) {
        this.depdt = depdt;
    }

    public String getNights() {
        return nights;
    }

    public void setNights(String nights) {
        this.nights = nights;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompanynm() {
        return Companynm;
    }

    public void setCompanynm(String companynm) {
        Companynm = companynm;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }
}
