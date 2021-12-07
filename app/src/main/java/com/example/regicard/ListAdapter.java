package com.example.regicard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter
{
    public ArrayList<com.example.regicard.DATA.ItemDTO> m_oData = new ArrayList<com.example.regicard.DATA.ItemDTO>();
    public ListAdapter(){                       //ListAdapter의 생성자
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
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        TextView oTextGuest = (TextView) convertView.findViewById(R.id.textGuest_nm);
        TextView oTextTelno = (TextView) convertView.findViewById(R.id.textTelno);
        TextView oTextBcnc_nm = (TextView) convertView.findViewById(R.id.textBcnc_nm);

        //Data Seet(listViewItemList)에서 position에 위치한 데이터 참조 획득
        com.example.regicard.DATA.ItemDTO itemDTO = m_oData.get(position);

        oTextGuest.setText(m_oData.get(position).Guestnm);
        oTextTelno.setText(m_oData.get(position).Telno);
        oTextBcnc_nm.setText(m_oData.get(position).Bcnc_nm);

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

    public void addItem(String guestnm, String telno, String bcnc_nm){                      //아이템 데이터 추가를 위한 함수
        com.example.regicard.DATA.ItemDTO item = new com.example.regicard.DATA.ItemDTO();
        item.setGuestnm(guestnm);
        item.setTelno(telno);
        item.setBcnc_nm(bcnc_nm);
        m_oData.add(item);
    }
}
