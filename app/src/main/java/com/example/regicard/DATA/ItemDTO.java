package com.example.regicard.DATA;

import android.view.View;

/**
 * Created by
 */

public class ItemDTO
{
    public String Guestnm;
    public String Telno;
    public String Bcnc_nm;
    public View.OnClickListener onClickListener;

    public String getGuestnm() {
        return Guestnm;
    }
    public void setGuestnm(String Guestnm) {
        this.Guestnm = Guestnm;
    }

    public String getTelno() {
        return Telno;
    }
    public void setTelno(String Telno) {
        this.Telno = Telno;
    }

    public String getBcnc_nm() {
        return Bcnc_nm;
    }
    public void setBcnc_nm(String Bcnc_nm) {
        this.Bcnc_nm = Bcnc_nm;
    }

}
