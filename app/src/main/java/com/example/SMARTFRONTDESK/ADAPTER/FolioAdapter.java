package com.example.SMARTFRONTDESK.ADAPTER;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SMARTFRONTDESK.DATA.FolioDTO;
import com.example.SMARTFRONTDESK.MainActivity;
import com.example.SMARTFRONTDESK.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FolioAdapter extends RecyclerView.Adapter<FolioAdapter.FolioViewHoder> {

    List<FolioDTO> list = new ArrayList<FolioDTO>();
    MainActivity activity;



    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    public void setList(List<FolioDTO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FolioViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folio_list_item, parent, false);
        return new FolioViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolioViewHoder holder, int position) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        FolioDTO item = list.get(position);
        holder.f_item_credit.setText(formatter.format(Integer.parseInt(item.getCredit())));
        holder.f_item_debit.setText(formatter.format(Integer.parseInt(item.getDebit())));
        holder.f_item_account.setText(item.getFolio_desc());
        holder.f_item_date.setText(item.getFolio_auditdate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FolioViewHoder extends RecyclerView.ViewHolder{
        TextView f_item_date, f_item_account, f_item_debit, f_item_credit;
        public FolioViewHoder(@NonNull View itemView) {
            super(itemView);
            f_item_date = itemView.findViewById(R.id.f_item_date);
            f_item_account = itemView.findViewById(R.id.f_item_account);
            f_item_debit = itemView.findViewById(R.id.f_item_debit);
            f_item_credit = itemView.findViewById(R.id.f_item_credit);
        }
    }
}
