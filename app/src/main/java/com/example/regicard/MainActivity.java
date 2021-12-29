
package com.example.regicard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;

import com.amitshekhar.DebugDB;
import com.example.regicard.DATA.FolioDTO;
import com.example.regicard.DATA.RegicardDTO;
import com.example.regicard.DATABASE.DBHelper;
import com.example.regicard.FRAGMENT.RegistrationCardFragment;
import com.example.regicard.FRAGMENT.RegifoliomainFragment;
import com.example.regicard.FRAGMENT.RegimainFragment;
import com.example.regicard.FRAGMENT.RegistartFragment;
import com.example.regicard.FRAGMENT.RegistrationCardListFragment;
import com.example.regicard.FRAGMENT.RegistrationCardOkFragment;
import com.example.regicard.FRAGMENT.RegistrationFolioFragment;
import com.example.regicard.FRAGMENT.RegistrationFolioOkFragment;
import com.example.regicard.FRAGMENT.RegistrationSettingFragment;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Bundle bundle;

    //강한별
    androidx.fragment.app.FragmentManager fmx;
    RegistartFragment fragment_registart; //시작화면
    RegistrationCardFragment fragment_registration_card;    //레지카드 조회화면
    RegistrationCardOkFragment fragment_registration_card_ok;   //레지카드 화면
    RegistrationCardListFragment fragment_registration_card_List;   //레지카드 고객리스트 화면
    RegistrationFolioFragment fragment_registration_folio;      //폴리오 조회화면
    RegistrationFolioOkFragment fragment_registration_folio_ok; //폴리오 화면
    RegistrationSettingFragment fragment_registration_setting;  //설정화면

    Button btn_folio, bth_home, btn_regiCard;

    AlertDialog.Builder alert;

    //DB
    DBHelper helper;
    SQLiteDatabase db;
    boolean check;

    public static String code000;   //호텔버전
    public static String code001;   //호텔명
    public static String code002;   //안내문구
    public static String code003;   //IP
    public static String code004;   //PORT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // 권한ID를 가져옵니다
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        // 권한이 열려있는지 확인
         if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
        // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
            }
            return;
         }



        //강한별
        fragment_registart = new RegistartFragment(); //프래그먼트 객채셍성
        fragment_registration_card_ok = new RegistrationCardOkFragment();
        fragment_registration_card = new RegistrationCardFragment();
        fragment_registration_card_List = new RegistrationCardListFragment();
        fragment_registration_folio = new RegistrationFolioFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new RegistartFragment()).commit();

        bundle = new Bundle();
        alert = new AlertDialog.Builder(this);
        //상단 액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //하단 네비게이션 버튼
        btn_folio = findViewById(R.id.btn_folio);
        bth_home = findViewById(R.id.bth_home);
        btn_regiCard = findViewById(R.id.btn_regiCard);

        //폴더 없을시 폴더 생성
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator+"SMARTFRONT");
        if(!file.exists())
            file.mkdir();

        //폴더 읽기 권한 설정
        file.setWritable(true);

        //파일 읽기 권한 설정
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator+"SMARTFRONT2"+File.separator+"SMARTFRONT.db");
        file.setWritable(true);


        //DB
        helper = DBHelper.getInstance(MainActivity.this); //디비얻어옴
        db = helper.getWritableDatabase();  //디비 열기




        //DataBase 조회
        startSql();

        //폴리오 화면 이동
        btn_folio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!check) {
                    alert.setTitle("알림");
                    alert.setMessage("setting을 모두 입력 해주세요.  " );
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                    TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                    msgView.setTextSize(20);
                    return;
                } else {
                    fragment_registration_folio.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, fragment_registration_folio).commit();
                }
            }
        });
        //레지카드화면이동
        btn_regiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!check) {
                    alert.setTitle("알림");
                    alert.setMessage("setting을 모두 입력 해주세요.  " );
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                    TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                    msgView.setTextSize(20);
                    return;
                } else {
                    fragment_registration_card.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, fragment_registration_card).commit();
                }
            }
        });
        //홈화면 이동
        bth_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new RegistartFragment()).commit();
            }
        });


    }

    //프래그먼트 화면전환
    @Nullable
    public void fragmentChange(String change, Bundle bundle){

        if(change == "MAIN") {

            //강한별
            //모든 프래그먼트 스택 지우기

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new RegistartFragment()).commit();

        } else if (change == "LIST"){
            List<RegicardDTO> list = (List<RegicardDTO>) bundle.getSerializable("list");
            fragment_registration_card_List.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.main_frame, fragment_registration_card_List).commit();

        } else if (change == "OK") {
            String ver = bundle.getString("ver");
            List<RegicardDTO> list = (List<RegicardDTO>) bundle.getSerializable("list");
            RegicardDTO item;
            if (list != null) {
                item = list.get(0);
            } else {
                item = null;
            }
            bundle.putString("check", "old");
            bundle.putString("ver", ver);
            bundle.putSerializable("item", (Serializable) item);
            fragment_registration_card_ok.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.main_frame, fragment_registration_card_ok).commit();
        } else if (change == "FOLIO") {

            List<FolioDTO> list = (List<FolioDTO>) bundle.getSerializable("list");
            bundle.putSerializable("itemF", (Serializable) list.get(0));
            fragment_registration_folio_ok = new RegistrationFolioOkFragment();
            fragment_registration_folio_ok.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.main_frame, fragment_registration_folio_ok).commit();
        } else if (change == "NEW") {

            bundle.putString("ver", bundle.getString("ver"));
            bundle.putString("check", "NEW");
            fragment_registration_card_ok = new RegistrationCardOkFragment();
            fragment_registration_card_ok.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.main_frame, fragment_registration_card_ok).commit();
        } else if ( change == "setting") {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new RegistrationSettingFragment()).commit();
        }
    }

    //바깥화면 클릭시 키보드 내림
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //시작할떄 데이터 있는지 조회
    public void startSql() {
        //0컬럼 조회
        String sql = "select * from COMMON where CODE like '00%' order by CODE";
        Cursor c = db.rawQuery(sql, null);
        if(c.getCount() >= 5) {
            String[] str = new String[c.getCount()];
            c.moveToFirst();    //첫번째행 이동
            for(int i=0; i<5; i++) {
                str[i] = c.getString(c.getColumnIndex("REMARK"));
                c.moveToNext(); //다음행으로이동
            }
            code000 = str[0];
            code001 = str[1];
            code002 = str[2];
            code003 = str[3];
            code004 = str[4];
            check = true;
        } else if(c.getCount() < 5 ){
            check = false;
        }
        //C컬럼조회
        String sqlC = "select * from COMMON where CODE like 'C%'order by CODE";
        Cursor cC = db.rawQuery(sql, null);
        if(cC.getCount() >= 5){
            String[] comcd = new String[c.getCount()];
            c.moveToFirst();    //첫번째행 이동
            for(int i=0; i<5; i++) {
                comcd[i] = c.getString(c.getColumnIndex("REMARK"));
                c.moveToNext(); //다음행으로이동
            }
            check = true;
        } else {
            check = false;
        }
    }


// 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        if(requestCode == 1000) {
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if(check_result == true) {

            } else {
                finish();
            }
        }
    }




}




