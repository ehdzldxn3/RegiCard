package com.example.SMARTFRONTDESK.FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SMARTFRONTDESK.ADAPTER.FolioAdapter;
import com.example.SMARTFRONTDESK.DATA.FolioDTO;
import com.example.SMARTFRONTDESK.MainActivity;
import com.example.SMARTFRONTDESK.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RegistrationFolioOkFragment extends Fragment {

    ViewGroup viewGroup;

    RecyclerView folio_rView;

    FolioAdapter adapter;

    TextView textbalance, textReserArrdate, textReserDepdate, textReserNo, textReserCustName;

    int balance;
    DecimalFormat formatter;
    FolioDTO item;

    String TAG = "FOLIO";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_folio_ok, container, false);

        //어댑터 초기화
        adapter = new FolioAdapter();
        adapter.setActivity((MainActivity) getActivity());

        //번들꺼내기
        Bundle bundle = getArguments();
        balance = bundle.getInt("balance");

        //textbalance 금액 자리수 설정 포맷 초기화
        formatter = new DecimalFormat("###,###");


        //메인액티비티 프래그먼트체인지 함수에서 받아온 데이터 저장
        item = (FolioDTO) bundle.getSerializable("itemF");

        //아이디 찾기
        findId();
        //아이템셋팅
        dataSet(item, balance);

        return viewGroup;
    }



    //뷰를 생성하고 그 다음에 데이터셋팅
    //데이터셋팅, 뷰셋팅
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //리싸이클러아이디 찾기
        folio_rView = view.findViewById(R.id.folio_rView);

        //받아옴
        List<FolioDTO> list = (ArrayList<FolioDTO>) getArguments().getSerializable("list");

        adapter.setList(list);
        folio_rView.setAdapter(adapter);
    }
    //아이디 찾기
    public void findId() {
        textReserArrdate = viewGroup.findViewById(R.id.textReserArrdate);
        textReserDepdate =  viewGroup.findViewById(R.id.textReserDepdate);
        textReserNo = viewGroup.findViewById(R.id.textReserNo);
        textReserCustName = viewGroup.findViewById(R.id.textReserCustName);
        textbalance = viewGroup.findViewById(R.id.textbalance);
    }
    //데이터셋팅
    public void dataSet(FolioDTO item, int balance) {
        textbalance.setText("BALANCE  :   " + formatter.format(balance)+" 원");
        textReserArrdate.setText(":  "+item.getReser_arrdate());
        textReserDepdate.setText(":  "+item.getReser_depdate());
        textReserNo.setText(":  "+item.getReser_no());
        textReserCustName.setText(":  " + item.getReser_cust_name());
    }

}
