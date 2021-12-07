
package com.example.regicard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.regicard.DATA.FolioDTO;
import com.example.regicard.DATA.RegicardDTO;
import com.example.regicard.FRAGMENT.RegistrationCardFragment;
import com.example.regicard.FRAGMENT.RegifoliomainFragment;
import com.example.regicard.FRAGMENT.RegimainFragment;
import com.example.regicard.FRAGMENT.RegistartFragment;
import com.example.regicard.FRAGMENT.RegistrationCardListFragment;
import com.example.regicard.FRAGMENT.RegistrationCardOkFragment;
import com.example.regicard.FRAGMENT.RegistrationFolioFragment;
import com.example.regicard.FRAGMENT.RegistrationFolioOkFragment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction tran;
    RegimainFragment fragment_regimain;
    RegistartFragment fragment_registart; //시작화면
    RegifoliomainFragment fragment_foliomain;
    String choice_flag = "", User_Name, company_code;

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public static Stack<RegistartFragment> fragmentStack;
    Bundle bundle;

    //강한별
    RegistrationCardFragment fragment_registration_card;
    RegistrationCardOkFragment fragment_registration_card_ok;
    RegistrationCardListFragment fragment_registration_card_List;
    RegistrationFolioFragment fragment_registration_folio;
    RegistrationFolioOkFragment fragment_registration_folio_ok;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        User_Name = getString(R.string.user_name);
        company_code = getString(R.string.company_code);

        fragment_registart = new RegistartFragment(); //프래그먼트 객채셍성
        fragmentStack = new Stack<>();
        fragmentStack.push(fragment_registart);
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        tran.replace(R.id.main_frame, fragment_registart);
//        tran.addToBackStack(null);    //강한별 삭제 처음 프래그먼트 스택 쌓을 필요없음
        tran.commit();

        //강한별
        fragment_registration_card_ok = new RegistrationCardOkFragment();
        fragment_registration_card = new RegistrationCardFragment();
        fragment_registration_card_List = new RegistrationCardListFragment();
        fragment_registration_folio = new RegistrationFolioFragment();
        bundle = new Bundle();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        bundle.putString("para_cmpny_cd", company_code);
        switch (item.getItemId()) {


            case R.id.regicard_btn:
                bundle = new Bundle();
                bundle.putString("para_cmpny_cd", company_code);
                bundle.putString("para_gubun", "R");
                bundle.putString("test", "테스트입니다.");
                fragmentStack.clear();
                fragment_registration_card = new RegistrationCardFragment(); //프래그먼트 객채셍성
                fm = getFragmentManager();

                //강한별
                //모든 프래그먼트 스택 지우기
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                //강한별
                tran = fm.beginTransaction();
                tran.replace(R.id.main_frame, fragment_registration_card);
                tran.addToBackStack(null);
                fragment_registration_card.setArguments(bundle);
                tran.commit();
                return true;



            case R.id.folio_btn:
                bundle = new Bundle();
                fragment_registration_folio.setArguments(bundle);
                bundle.putString("para_cmpny_cd", company_code);
                bundle.putString("para_gubun", "R");
                bundle.putString("test", "테스트입니다.");
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.main_frame, fragment_registration_folio).commit();
                return true;

            case R.id.exit_btn:
                exitBtn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

            fragmentStack.clear();
            fm.popBackStackImmediate();
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new RegistartFragment()).commit();

        } else if (change == "LIST"){
            List<RegicardDTO> list = (List<RegicardDTO>) bundle.getSerializable("list");

            fragment_registration_card_List = new RegistrationCardListFragment();
            fragment_registration_card_List.setArguments(bundle);
            fragmentStack.clear();
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
            bundle.putString("ver", ver);
            bundle.putSerializable("item", (Serializable) item);
            fragment_registration_card_ok = new RegistrationCardOkFragment();
            fragment_registration_card_ok.setArguments(bundle);
            fragmentStack.clear();
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.main_frame, fragment_registration_card_ok).commit();
        } else if (change == "FOLIO") {
            List<FolioDTO> list = (List<FolioDTO>) bundle.getSerializable("list");
            fragment_registration_folio_ok = new RegistrationFolioOkFragment();
            fragment_registration_folio_ok.setArguments(bundle);
            fragmentStack.clear();
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.main_frame, fragment_registration_folio_ok).commit();
        }
    }
}




