package com.example.regicard.DATA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegifolioRestDTO {


    @SerializedName("RESVE_NO")
    @Expose
    public String Resve_no;

    @SerializedName("SEQ")
    @Expose
    public String Seq;

    @SerializedName("DATE")
    @Expose
    public String Date;

    @SerializedName("ROOMNO")
    @Expose
    public String Roomno;

    @SerializedName("ACCOUNT")
    @Expose
    public String Account;

    @SerializedName("DEBIT")
    @Expose
    public String Debit;

    @SerializedName("CREDIT")
    @Expose
    public String Credit;

    @SerializedName("BALANCE")
    @Expose
    public String Balance;

    public String getResve_no() { return Resve_no; }
    public String getSeq() { return Seq; }
    public String getRoomno() { return Roomno; }

    public String getDate() { return Date; }
    public void setDate(String Date) {this.Date = Date; }

    public String getAccount() {
        return Account;
    }
    public void setAccount(String Account) {this.Account = Account; }

    public String getDebit() { return Debit; }
    public void setDebit(String Debit) {this.Debit = Debit; }

    public String getCredit() { return Credit; }
    public void setCredit(String Credit) {this.Credit = Credit; }

    public String getBalance() { return Balance; }
    public void setBalance(String Balance) {this.Balance = Balance; }

}
