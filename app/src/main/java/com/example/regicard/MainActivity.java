
package com.example.regicard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

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

    FragmentManager fm;
    FragmentTransaction tran;
    RegimainFragment fragment_regimain;

    RegifoliomainFragment fragment_foliomain;
    String choice_flag = "", User_Name, company_code;

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");


    Bundle bundle;

    //강한별
    androidx.fragment.app.FragmentManager fmx;
    RegistartFragment fragment_registart; //시작화면
    RegistrationCardFragment fragment_registration_card;
    RegistrationCardOkFragment fragment_registration_card_ok;
    RegistrationCardListFragment fragment_registration_card_List;
    RegistrationFolioFragment fragment_registration_folio;
    RegistrationFolioOkFragment fragment_registration_folio_ok;
    RegistrationSettingFragment fragment_registration_setting;

    Button btn_folio, bth_home, btn_regiCard, btn_setting;




    //DB
    DBHelper helper;
    SQLiteDatabase db;
    String TAG =  "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        User_Name = getString(R.string.user_name);
        company_code = getString(R.string.company_code);

        fragment_registart = new RegistartFragment(); //프래그먼트 객채셍성


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new RegistartFragment()).commit();



        //강한별
        fragment_registration_card_ok = new RegistrationCardOkFragment();
        fragment_registration_card = new RegistrationCardFragment();
        fragment_registration_card_List = new RegistrationCardListFragment();
        fragment_registration_folio = new RegistrationFolioFragment();

        bundle = new Bundle();
        
        //상단 액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //하단 네비게이션 버튼
        btn_folio = findViewById(R.id.btn_folio);
        bth_home = findViewById(R.id.bth_home);
        btn_regiCard = findViewById(R.id.btn_regiCard);
        btn_setting = findViewById(R.id.btn_setting);

        //DB
        helper = DBHelper.getInstance(MainActivity.this); //디비얻어옴
        db = helper.getWritableDatabase();  //디비 열기
        String sql = "select * from COMMON;";   //
        Cursor c = db.rawQuery(sql, null);
        //데이터 없으면 알림창
        if(c.getCount() == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("알림");
            alert.setMessage("기본정보를 셋팅해주세요!.  " );
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
            TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
            msgView.setTextSize(20);
        }







        //폴리오 화면 이동
        btn_folio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_registration_folio.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, fragment_registration_folio).commit();
            }
        });
        //레지카드화면이동
        btn_regiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_registration_card.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, fragment_registration_card).commit();
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

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new RegistrationSettingFragment()).commit();
            }
        });

    }




//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        bundle.putString("para_cmpny_cd", company_code);
//
//        switch (item.getItemId()) {
//
//            case R.id.regicard_btn:
//                fragment_registration_card.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                        .replace(R.id.main_frame, fragment_registration_card).commit();
//
//                return true;
//
//            case R.id.folio_btn:
//                fragment_registration_folio.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                        .replace(R.id.main_frame, fragment_registration_folio).commit();
//                return true;
//
//            case R.id.exit_btn:
//                getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                        .replace(R.id.main_frame, test).commit();
////                exitBtn();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void exitBtn() {
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }

    // 강한별 주석처리
    //프래그먼트 스택 각화면에서 관리
//    @Override
//    public void onBackPressed() {
//        if (fragmentStack.size()  < 0) {
//            //종료하는것
//            exitBtn();
//            return;
//        }
//        if (!fragmentStack.isEmpty()) {
//            fragmentStack.pop();
//            super.onBackPressed();
//        }
//
//    }

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
    //모든 스택 다 지우기
    private void clearBackStack() {
        final androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }
}




