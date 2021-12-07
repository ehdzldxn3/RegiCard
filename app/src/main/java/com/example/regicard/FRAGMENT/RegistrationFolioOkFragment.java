package com.example.regicard.FRAGMENT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.regicard.ADAPTER.FolioAdapter;
import com.example.regicard.ADAPTER.RegistrationAdapter;
import com.example.regicard.DATA.FolioDTO;
import com.example.regicard.DATA.RegicardDTO;
import com.example.regicard.MainActivity;
import com.example.regicard.R;

import java.util.ArrayList;
import java.util.List;

public class RegistrationFolioOkFragment extends Fragment {

    ViewGroup viewGroup;

    RecyclerView folio_rView;

    FolioAdapter adapter;

    TextView textbalance;

    int balance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_folio_ok, container, false);
        adapter = new FolioAdapter();
        adapter.setActivity((MainActivity) getActivity());
        Bundle bundle = getArguments();
        balance = bundle.getInt("balance");
        Log.e("TAG", "onCreateView: "+balance );
        textbalance = viewGroup.findViewById(R.id.textbalance);


        //textbalance.setText(balance.toString);



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


}
