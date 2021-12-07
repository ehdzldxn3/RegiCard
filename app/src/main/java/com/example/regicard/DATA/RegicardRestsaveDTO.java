package com.example.regicard.DATA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegicardRestsaveDTO {

    @SerializedName("CMPNY_CD")
    @Expose
    public String save_Cmpny_cd;

    @SerializedName("RESVE_NO")
    @Expose
    public String save_Resve_no;

    @SerializedName("SEQ")
    @Expose
    public String save_Seq;

    @SerializedName("PROFLNO")
    @Expose
    public String save_Proflno;

    public String getsaveCmpny_cd() {return save_Resve_no;}
    public String getsaveResve_no() {return save_Resve_no;}
    public String getsaveSeq() {return save_Seq;}
    public String getsaveProflnoSeq() {return save_Proflno;}

}