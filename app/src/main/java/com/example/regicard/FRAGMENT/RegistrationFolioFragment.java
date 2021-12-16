package com.example.regicard.FRAGMENT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.regicard.DATA.FolioDTO;
import com.example.regicard.DATA.RegicardDTO;
import com.example.regicard.MainActivity;
import com.example.regicard.R;
import com.example.regicard.RETROFIT.RetrofitService;
import com.example.regicard.RETROFIT.ServiceGenerator;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationFolioFragment extends Fragment {

    //view 생성
    ViewGroup viewGroup;

    Button btnFlioOK;

    EditText editFolioRoomNo;

    MainActivity activity;

    String TAG = "Folio";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_folio, container, false);
        //전화면에서 받아온 데이터
        Bundle bundle = getArguments();

        editFolioRoomNo = viewGroup.findViewById(R.id.editFolioRoomNo);
        btnFlioOK = viewGroup.findViewById(R.id.btnFlioOK);
        activity = (MainActivity) getActivity();

        //확인버튼 클릭리스너너
        btnFlioOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
                Call<List<FolioDTO>> request = service.Folio(editFolioRoomNo.getText().toString());
                request.enqueue(new Callback<List<FolioDTO>>() {
                    @Override
                    public void onResponse(Call<List<FolioDTO>> call, Response<List<FolioDTO>> response) {
                        List<FolioDTO> list = response.body();

                        if(list.size() == 0) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("알림");
                            alert.setMessage("객실번호를 확인해주세요.  " );
                            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();     //닫기
                                }
                            });
                            AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                            TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                            msgView.setTextSize(20);
                        } else {
                            int credit = 0, debit = 0;
                            int balance = 0;
                            for(int i=0; i<list.size(); i++){
                                credit += Integer.parseInt(list.get(i).getCredit());
                                debit += Integer.parseInt(list.get(i).getDebit());
                            }
                            editFolioRoomNo.setText("");
                            bundle.putInt("balance",credit-debit);
                            bundle.putSerializable("list", (Serializable) list);
                            activity.fragmentChange("FOLIO", bundle);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FolioDTO>> call, Throwable t) {
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
            }
        });

        return viewGroup;
    }
}
