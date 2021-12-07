package com.example.regicard.DATA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegicardRestDTO {


    @SerializedName("RESVE_NO")
    @Expose
    public String Resve_no;

    @SerializedName("SEQ")
    @Expose
    public String Seq;

    @SerializedName("PROFLNO")
    @Expose
    public String Proflno;

    @SerializedName("GUEST_NM")
    @Expose
    public String Guest_nm;

    @SerializedName("TELNO")
    @Expose
    public String Telno;

    @SerializedName("ROOMNO")
    @Expose
    public String Roomno;

    @SerializedName("PASSPORT")
    @Expose
    public String Passport;

    @SerializedName("COUNTRY")
    @Expose
    public String Country;

    @SerializedName("ADRES")
    @Expose
    public String Adres;

    @SerializedName("BCNC_NM")
    @Expose
    public String Bcnc_nm;

    @SerializedName("ARRDATE")
    @Expose
    public String Arrdate;

    @SerializedName("DEPTDATE")
    @Expose
    public String Deptdate;

    public String getResve_no() { return Resve_no; }
    public String getSeq() { return Seq; }
    public String getProflno() { return Proflno; }
    public String getGuestnm() { return Guest_nm; }
    public String getTelno() {
        return Telno;
    }
    public String getRoomno() {
        return Roomno;
    }
    public String getPassport() {
        return Passport;
    }
    public String getCountry() {
        return Country;
    }
    public String getAdres() { return Adres; }
    public String getBcnc_name() {
        return Bcnc_nm;
    }
    public String getArrdate() {
        return Arrdate;
    }
    public String getDeptdate() {
        return Deptdate;
    }
}
