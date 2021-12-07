package com.example.regicard.FRAGMENT;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.regicard.DATA.FolioDTO;
import com.example.regicard.DATA.RegifolioRestDTO;
import com.example.regicard.ListFolioAdapter;
import com.example.regicard.R;
import com.example.regicard.RETROFIT.RetrofitService;
import com.example.regicard.RETROFIT.ServiceGenerator;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegifoliomainFragment extends Fragment {
    View view;
    FragmentManager fm;
    FragmentTransaction tran;
    RegifoliomainFragment fragment_foliomain;
    String cmpny_cd,gubun;
    String tmp_balance,resve_no,seq;
    TextView text_balance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_foliomain, container, false);

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        HashMap<String, Object> input = new HashMap<>(); // Request put

        Bundle bundle = getArguments();
        cmpny_cd = bundle.getString("para_cmpny_cd");
        gubun = bundle.getString("para_gubun");
        resve_no = bundle.getString("para_resve_no");
        seq = bundle.getString("para_seq");

        input.put("cmpny_cd", cmpny_cd);
        input.put("gubun", gubun);
        input.put("resve_no", resve_no);
        input.put("seq", seq);

        text_balance = (TextView) view.findViewById(R.id.balance);

        Call<List<RegifolioRestDTO>> request = service.Regifolio_con(input);         //  post 전송, List로 받는다.
        request.enqueue(new Callback<List<RegifolioRestDTO>>() {
            @Override
            public void onResponse(Call<List<RegifolioRestDTO>> call, Response<List<RegifolioRestDTO>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                final List<RegifolioRestDTO> RegifolioRestDTOs = response.body();             // 서버에서 받은 데이타 List에 담기
                ListView listview;
                ListFolioAdapter fadapter;
                fadapter = new ListFolioAdapter();                  //Adapter 생성
                listview = (ListView) view.findViewById(R.id.FOLIO);
                listview.setAdapter(fadapter);

                for (RegifolioRestDTO RegifolioRestDTO : RegifolioRestDTOs) {                // RegicardDTOs 에 몇번째 값인지 cnt 값 들어 있음
//                    FolioDTO fItem = new FolioDTO();
//                    tmp_balance = RegifolioRestDTO.getBalance();
//                    fItem.Date = RegifolioRestDTO.getDate();
//                    fItem.Account = RegifolioRestDTO.getAccount();
//                    fItem.Debit = RegifolioRestDTO.getDebit();
//                    fItem.Credit = RegifolioRestDTO.getCredit();
//                    fadapter.addItem(fItem.Date, fItem.Account, fItem.Debit, fItem.Credit);
                }
                text_balance.setText(tmp_balance);
            }

            @Override
            public void onFailure(Call<List<RegifolioRestDTO>> call, Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("알림");
                alert.setMessage("통신 에러 입니다.  " + t);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                msgView.setTextSize(20);
            }
        });
      return view;
    }               // onCreate End

}

