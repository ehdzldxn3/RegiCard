package com.example.regicard.DATA;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class FolioDTO implements Serializable {

    @SerializedName("RESER_NO") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String reser_no;

    @SerializedName("RESER_SEQ") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String reser_seq;

    @SerializedName("SEQ_NO") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String seq_no;

    @SerializedName("FOLIO_AUDITDATE") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String folio_auditdate;

    @SerializedName("FOLIO_TRANSCODE") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String folio_transcode;

    @SerializedName("FOLIO_DESC") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String folio_desc;

    @SerializedName("CREDIT") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String credit;

    @SerializedName("DEBIT") //json 매칭 될떄 이름명시목적
    @Expose //null일때 생략하는것
    public String debit;

    public String getReser_no() {
        return reser_no;
    }

    public void setReser_no(String reser_no) {
        this.reser_no = reser_no;
    }

    public String getReser_seq() {
        return reser_seq;
    }

    public void setReser_seq(String reser_seq) {
        this.reser_seq = reser_seq;
    }

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }

    public String getFolio_auditdate() {
        return folio_auditdate;
    }

    public void setFolio_auditdate(String folio_auditdate) {
        this.folio_auditdate = folio_auditdate;
    }

    public String getFolio_transcode() {
        return folio_transcode;
    }

    public void setFolio_transcode(String folio_transcode) {
        this.folio_transcode = folio_transcode;
    }

    public String getFolio_desc() {
        return folio_desc;
    }

    public void setFolio_desc(String folio_desc) {
        this.folio_desc = folio_desc;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }
}
