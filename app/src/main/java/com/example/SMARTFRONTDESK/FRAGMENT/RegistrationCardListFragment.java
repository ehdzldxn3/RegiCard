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

import com.example.SMARTFRONTDESK.DATA.RegicardDTO;
import com.example.SMARTFRONTDESK.MainActivity;
import com.example.SMARTFRONTDESK.R;
import com.example.SMARTFRONTDESK.ADAPTER.RegistrationAdapter;

import java.util.ArrayList;
import java.util.List;

public class RegistrationCardListFragment extends Fragment {

    //view 생성
    ViewGroup viewGroup;

    RecyclerView rView;

    RegistrationAdapter adapter;

    String ver;

    TextView textName, textTel, textCompany, text001;

    //뷰를 만들고
    //초기화
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_list, container, false);
        adapter = new RegistrationAdapter();
        adapter.setActivity((MainActivity) getActivity());
        Bundle bundle = getArguments();

        ver = bundle.getString("ver");

        //아이디 찾기
        textName = viewGroup.findViewById(R.id.textName);
        textTel = viewGroup.findViewById(R.id.textTel);
        textCompany = viewGroup.findViewById(R.id.textCompany);
        text001 = viewGroup.findViewById(R.id.text001);



        //버전체크
        if(bundle.getString("ver") == "KOR") {
            verKorchange();
        }


        return viewGroup;
    }

    //뷰를 생성하고 그 다음에 데이터셋팅
    //데이터셋팅, 뷰셋팅
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //리싸이클러아이디 찾기
        rView = view.findViewById(R.id.rView);

        //받아옴
        List<RegicardDTO> list = (ArrayList<RegicardDTO>)getArguments().getSerializable("list");

        adapter.setList(list);
        adapter.setVer(ver);
        rView.setAdapter(adapter);
    }

    public void verKorchange() {
        textName.setText("이름");
        textTel.setText("전화번호");
        textCompany.setText("회사");
        text001.setText(MainActivity.code001);
    }
}
