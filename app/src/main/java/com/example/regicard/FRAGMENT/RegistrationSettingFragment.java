package com.example.regicard.FRAGMENT;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.regicard.ADAPTER.FolioAdapter;
import com.example.regicard.ADAPTER.OnSettinItemClickListener;
import com.example.regicard.ADAPTER.RegistrationAdapter;
import com.example.regicard.ADAPTER.SettingAdapter;
import com.example.regicard.DATA.CommonDTO;
import com.example.regicard.DATA.RegicardDTO;
import com.example.regicard.DATABASE.DBHelper;
import com.example.regicard.MainActivity;
import com.example.regicard.R;

import java.util.ArrayList;
import java.util.List;

public class RegistrationSettingFragment extends Fragment {

    //view 생성
    ViewGroup viewGroup;
    
    //메인 액티비티
    MainActivity activity;
    RegistrationSettingFragment fragment;

    //DB
    DBHelper helper;
    SQLiteDatabase db;

    //에딧
    EditText editCode, editRemark;

    //버튼
    Button btnN, btnS, btnD;

    //insert update delete 체크용
    String check;

    RecyclerView srview;
    SettingAdapter adapter;
    List<CommonDTO> list;
    

    //알림창
    AlertDialog.Builder alert;
    String TAG = "레지 세팅 프래그먼트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_setting, container, false);



        //액티비티 초기화
        activity = (MainActivity) getContext();
        fragment = new RegistrationSettingFragment();

        //아이디찾기
        editCode = viewGroup.findViewById(R.id.editCode);
        editRemark = viewGroup.findViewById(R.id.editRemark);
        btnN = viewGroup.findViewById(R.id.btnN);
        btnS = viewGroup.findViewById(R.id.btnS);
        btnD = viewGroup.findViewById(R.id.btnD);


        alert = new AlertDialog.Builder(getActivity());

        ///////////////////////////////////////////

        //세이브 버튼
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = editCode.getText().toString();
                String remark = editRemark.getText().toString();

                if(code.equals("") || remark.equals("")) {  //데이터 미 입력시 거르는곳
                    alert.setTitle("알림");
                    alert.setMessage("CODE & REMARK 입력 해주세요.  " );
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                    TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                    msgView.setTextSize(20);
                    return;
                } else {     //코드가 중복 안되게 검사

                    //select * from COMMON where CODE ='code';
                    String sql = "select * from COMMON where CODE =" + "'" +code +"';";

                    Cursor c = db.rawQuery(sql, null);
                    if(c.getCount()>=1) {
                        alert.setTitle("알림");
                        alert.setMessage("CODE는 중복될수 없습니다.  " );
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();     //닫기
                            }
                        });
                        AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                        TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                        msgView.setTextSize(20);
                        return;
                    }
                }
                    ContentValues values = new ContentValues();
                    values.put("CODE", code);
                    values.put("REMARK", remark);
                    db.insert("COMMON",null,values);
                    dbList();
                    check = "save";
            }
        });

        //삭제버튼
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = "delete";
                String code = editCode.getText().toString();

                //데이터 미 입력시 거르는곳
                if(code.equals("")) {
                    alert.setTitle("알림");
                    alert.setMessage("CODE 입력 해주세요.  " );
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                    TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                    msgView.setTextSize(20);
                    return;
                }
                db.delete("COMMON","CODE=?",new String[]{code});
            }
        });

        //NEW 버튼
        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = "new";
                editCode.setText("");
                editRemark.setText("");
            }
        });
        return viewGroup;
    }

    //어뎁터 셋팅
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //리싸이클러아이디 찾기
        srview = view.findViewById(R.id.srview);
        list = new ArrayList<>();
        //디비 열기
        helper = DBHelper.getInstance(activity);
        db = helper.getWritableDatabase();
        dbList();
    }

    //리싸이클러뷰 클릭 이벤트
    public void onItemClick(CommonDTO item) {
        check = "update";
        editCode.setText(item.getCode());
        editRemark.setText(item.getRemark());
    }

    //DB 테이블 전체조회
    public void dbList() {

        //리스트 초기화
        list = new ArrayList<>();
        //리스트 생성
        String sql = "select * from COMMON order by CODE;";
        Cursor c = db.rawQuery(sql, null);
        
        c.moveToFirst();    //첫번째행 이동

        for(int i=0; i<c.getCount(); i++) {
            CommonDTO dto = new CommonDTO();
            dto.setCode(c.getString(c.getColumnIndex("CODE")));
            dto.setRemark(c.getString(c.getColumnIndex("REMARK")));
            list.add(dto);
            c.moveToNext(); //다음행으로이동
        }

        adapter = new SettingAdapter((MainActivity)getActivity(), this );
        adapter.setList(list);
        srview.setAdapter(adapter);
    }



}
