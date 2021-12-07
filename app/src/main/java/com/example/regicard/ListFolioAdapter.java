package com.example.regicard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.regicard.DATA.FolioDTO;

import java.util.ArrayList;

public class ListFolioAdapter extends BaseAdapter
{
    public ArrayList<FolioDTO> m_oData = new ArrayList<FolioDTO>();
    public ListFolioAdapter(){                       //ListAdapter의 생성자
    }

    // Adapter에 사용되는 데이터의 개수를 리턴, : 필수 구현
    @Override
    public int getCount() {
        return m_oData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        //"listview_item" Layout을 infalte하여 convertView 참조 획득.
        if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_folio, parent, false);
        }
        TextView oTextDate = (TextView) convertView.findViewById(R.id.textdate);
        TextView oTextaccount = (TextView) convertView.findViewById(R.id.textaccount);
        TextView oTextdebit = (TextView) convertView.findViewById(R.id.textdebit);
        TextView oTextCredit = (TextView) convertView.findViewById(R.id.textcredit);

        //Data Seet(listViewItemList)에서 position에 위치한 데이터 참조 획득
        FolioDTO folioDTO= m_oData.get(position);

//        oTextDate.setText(m_oData.get(position).Date);
//        oTextaccount.setText(m_oData.get(position).Account);
//        oTextdebit.setText(m_oData.get(position).Debit);
//        oTextCredit.setText(m_oData.get(position).Credit);

        return convertView;
    }

    //지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return m_oData.get(position);
    }

    public void addItem(String date, String account, String debit, String credit){                      //아이템 데이터 추가를 위한 함수
//        FolioDTO item = new FolioDTO();
//        item.setDate(date);
//        item.setAccount(account);
//        item.setDebit(debit);
//        item.setCredit(credit);
//        m_oData.add(item);
    }
}
