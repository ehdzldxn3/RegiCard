package com.example.SMARTFRONTDESK.DATA;

import com.google.gson.annotations.Expose;


public class CommonDTO {

    @Expose //null일때 생략하는것
    public int _id;

    @Expose //null일때 생략하는것
    public String code;   //코드

    @Expose //null일때 생략하는것
    public String remark;   //내용


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
