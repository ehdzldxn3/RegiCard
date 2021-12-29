package com.example.SMARTFRONTDESK.ADAPTER;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SMARTFRONTDESK.DATA.RegicardDTO;
import com.example.SMARTFRONTDESK.FRAGMENT.RegistrationCardOkFragment;
import com.example.SMARTFRONTDESK.MainActivity;
import com.example.SMARTFRONTDESK.R;

import java.util.ArrayList;
import java.util.List;

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.RegistrationViewHoder> {

    List<RegicardDTO> list = new ArrayList<RegicardDTO>();

    Fragment fragment_registration_card_ok;
    MainActivity activity;

    String ver;

    @NonNull
    @Override
    public RegistrationViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registration_list_item, parent, false);
        return new RegistrationViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistrationViewHoder holder, int position) {
        RegicardDTO item = list.get(position);
        holder.textName.setText(item.getName());
        holder.textTel.setText(item.getTel());
        holder.textCompanynm.setText(item.getCompanynm());

        //클릭이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item",item);
                bundle.putString("ver", ver);
                fragment_registration_card_ok = new RegistrationCardOkFragment();
                fragment_registration_card_ok.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.main_frame, fragment_registration_card_ok).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //뷰홀더클래스 : 리싸이클러뷰에서 한줄의 아이템뷰
    class RegistrationViewHoder extends RecyclerView.ViewHolder{
        TextView textName, textTel, textCompanynm;

        public RegistrationViewHoder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textTel = itemView.findViewById(R.id.textTel);
            textCompanynm = itemView.findViewById(R.id.textCompanynm);
        }
    }



    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    public void setList(List<RegicardDTO> list) {
        this.list = list;
    }
    public void setVer(String ver) {
        this.ver = ver;
    }

}


