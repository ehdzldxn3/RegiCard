package com.example.SMARTFRONTDESK.FRAGMENT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.SMARTFRONTDESK.DATA.FolioDTO;
import com.example.SMARTFRONTDESK.MainActivity;
import com.example.SMARTFRONTDESK.R;
import com.example.SMARTFRONTDESK.RETROFIT.RetrofitService;
import com.example.SMARTFRONTDESK.RETROFIT.ServiceGenerator;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationFolioFragment extends Fragment {

    //view 생성
    ViewGroup viewGroup;

    Button btnFlioOK;

    EditText editFolioRoomNo;

    TextView text001, text002;

    MainActivity activity;

    AlertDialog.Builder alert;

    String TAG = "Folio";

    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_folio, container, false);
        //전화면에서 받아온 데이터
        Bundle bundle = getArguments();

        //알러트창

        editFolioRoomNo = viewGroup.findViewById(R.id.editFolioRoomNo);
        text001 = viewGroup.findViewById(R.id.text001);
        text002 = viewGroup.findViewById(R.id.text002);
        btnFlioOK = viewGroup.findViewById(R.id.btnFlioOK);
        activity = (MainActivity) getActivity();


        text001.setText(MainActivity.code001);
        text002.setText(MainActivity.code002);



        //확인버튼 클릭리스너너
        btnFlioOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(getContext());
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Loading...");

                dialog.show();

                RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
                Call<List<FolioDTO>> request = service.Folio(editFolioRoomNo.getText().toString());
                request.enqueue(new Callback<List<FolioDTO>>() {
                    @Override
                    public void onResponse(Call<List<FolioDTO>> call, Response<List<FolioDTO>> response) {
                        dialog.dismiss();

                        List<FolioDTO> list = response.body();
                        if(list.size() == 0) {
                            alert = new AlertDialog.Builder(getActivity());
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
                        dialog.dismiss();
                        alert = new AlertDialog.Builder(getActivity());
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
