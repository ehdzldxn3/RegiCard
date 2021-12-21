package com.example.regicard.ADAPTER;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.regicard.DATA.CommonDTO;
import com.example.regicard.DATA.FolioDTO;
import com.example.regicard.FRAGMENT.RegistrationCardOkFragment;
import com.example.regicard.FRAGMENT.RegistrationSettingFragment;
import com.example.regicard.MainActivity;
import com.example.regicard.R;

import java.util.ArrayList;
import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHoder> {

    List<CommonDTO> list = new ArrayList<CommonDTO>();
    MainActivity activity;
    RegistrationSettingFragment fragment;

    public void setList(List<CommonDTO> list) {
        this.list = list;
    }

    public SettingAdapter(MainActivity activity, RegistrationSettingFragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SettingViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_list_item, parent, false);
        return new SettingViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHoder holder, int position) {
        CommonDTO item = list.get(position);
        holder.code.setText(item.getCode());
        holder.remark.setText(item.getRemark());

        //클릭이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.onItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //아이디 찾기
    public class SettingViewHoder extends RecyclerView.ViewHolder {
        TextView code, remark;
        public SettingViewHoder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.code);
            remark = itemView.findViewById(R.id.remark);
        }
    }





}
