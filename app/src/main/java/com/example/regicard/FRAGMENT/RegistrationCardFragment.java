package com.example.regicard.FRAGMENT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.regicard.DATA.RegicardDTO;
import com.example.regicard.MainActivity;
import com.example.regicard.R;
import com.example.regicard.RETROFIT.RetrofitService;
import com.example.regicard.RETROFIT.ServiceGenerator;


import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationCardFragment extends Fragment {

    //view 생성
    ViewGroup viewGroup;

    //버튼
    Button btnOK, btnNew;

    //에딧텍스트
    EditText editText1, editText2, editText3;

    //checkBox
    CheckBox verCheck;  //버전체크

    //
    Bundle bundle = new Bundle();

    MainActivity activity;

    String TAG = "RegistrationCardFragment ";
    String res, name, phone, ver; //예약번호, 전화번호, 이름, 버전

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_card, container, false);
        //전화면에서 받아온 데이터
        Bundle bundle = getArguments();

        btnOK = viewGroup.findViewById(R.id.btnOK);
        btnNew = viewGroup.findViewById(R.id.btnNew);
        editText1 = viewGroup.findViewById(R.id.editText1); //예약번호
        editText2 = viewGroup.findViewById(R.id.editText2); //이름
        editText3 = viewGroup.findViewById(R.id.editText3); //전화번호
        verCheck = viewGroup.findViewById(R.id.verCheck); //버전체크



        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
                res = editText1.getText().toString();
                name = editText2.getText().toString();
                phone = editText3.getText().toString();
                ver = verCheck.isChecked() ? "ENG" : "KOR";

                if(res.equals("") && phone.equals("") && name.equals("")){
                    Toast.makeText(getActivity(), "예약번호 & 이름 & 전화번호 한개라도 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Call<List<RegicardDTO>> request = service.Regicard(res,phone,name);
                    request.enqueue(new Callback<List<RegicardDTO>>() {
                        @Override
                        public void onResponse(Call<List<RegicardDTO>> call, Response<List<RegicardDTO>> response) {
                            List<RegicardDTO> list = response.body(); //데이터 저장

                            if(list.size() == 1) {
                                bundle.putString("ver", ver);
                                bundle.putSerializable("list", (Serializable) list);
                                activity.fragmentChange("OK", bundle);
                                editText1.setText("");
                                editText2.setText("");
                                editText3.setText("");
                            } else if(list.size() > 1){
                                bundle.putString("ver", ver);
                                bundle.putSerializable("list", (Serializable) list);
                                activity.fragmentChange("LIST", bundle);
                                editText1.setText("");
                                editText2.setText("");
                                editText3.setText("");
                            } else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setTitle("알림");
                                alert.setMessage("데이터가 없습니다.  " );
                                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                                TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                                msgView.setTextSize(20);
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        }, 1000);
                            }
                        }
                        @Override
                        public void onFailure(Call<List<RegicardDTO>> call, Throwable t) {
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
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ver = verCheck.isChecked() ? "ENG" : "KOR";
                bundle.putString("ver", ver);
                activity.fragmentChange("NEW", bundle);
            }
        });
        return viewGroup;
    }
}
