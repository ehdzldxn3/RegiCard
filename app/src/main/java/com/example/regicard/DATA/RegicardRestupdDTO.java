package com.example.regicard.DATA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegicardRestupdDTO {

    @SerializedName("CMPNY_CD")
    @Expose
    public String save_Cmpny_cd;

    @SerializedName("RESVE_NO")
    @Expose
    public String save_Resve_no;

    @SerializedName("SEQ")
    @Expose
    public String save_Seq;

    @SerializedName("isSIGN")
    @Expose
    public String save_isSIGN;

    @SerializedName("isAGREE")
    @Expose
    public String save_isAGREE;

    public String getsaveCmpny_cd() {return save_Resve_no;}
    public String getsaveResve_no() {return save_Resve_no;}
    public String getsaveSeq() {return save_Seq;}
    public String getsaveisSIGN() {return save_isSIGN;}
    public String getsaveisAGREE() {return save_isAGREE;}

}