package com.example.regicard.FRAGMENT;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.regicard.DATA.RegicardRestDTO;
import com.example.regicard.ListAdapter;
import com.example.regicard.MainActivity;
import com.example.regicard.R;
import com.example.regicard.RETROFIT.RetrofitService;
import com.example.regicard.RETROFIT.ServiceGenerator;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegimainFragment extends Fragment {

    View view;
    FragmentManager fm;
    FragmentTransaction tran;
    RegistartFragment fragment_regicard;
    RegistartFragment fragment_foliomain;
    String cmpny_cd, gubun;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_regimain, container, false);

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        HashMap<String, Object> input = new HashMap<>(); // Request put

        Bundle bundle = getArguments();
        cmpny_cd = bundle.getString("para_cmpny_cd");
        gubun = bundle.getString("para_gubun");
        input.put("cmpny_cd", cmpny_cd);
        input.put("gubun_cd", gubun);

        Call<List<RegicardRestDTO>> request = service.Regicard_con(input);         //  post 전송, List로 받는다.
        request.enqueue(new Callback<List<RegicardRestDTO>>() {
            @Override
            public void onResponse(Call<List<RegicardRestDTO>> call, Response<List<RegicardRestDTO>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                final List<RegicardRestDTO> RegicardRestDTOs = response.body();             // 서버에서 받은 데이타 List에 담기

                ListView listview;
                ListAdapter adapter;
                //Adapter 생성
                adapter = new ListAdapter();                  //Adapter 생성
                listview = (ListView) view.findViewById(R.id.LH);

                listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listview.setAdapter(adapter);

                if (RegicardRestDTOs.size() > 1) {
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {      // 조회된 리스트뷰 선택
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            if (gubun == "R") {
                                rstartintent(position, RegicardRestDTOs);
                            }else {
                                fstartintent(position, RegicardRestDTOs);
                            }
                        }
                    });

                } else if (RegicardRestDTOs.size() == 1) {
                    if (gubun == "R") {
                        rstartintent(0, RegicardRestDTOs);
                    }else{
                        fstartintent(0, RegicardRestDTOs);
                    }

                } else {
                    Toast toast = Toast.makeText(getActivity(), "고객이 없습니다.", Toast.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView msgTextView = (TextView) group.getChildAt(0);
                    msgTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                    toast.show();
                }

                for (RegicardRestDTO RegicardRestDTO : RegicardRestDTOs) {                // RegicardDTOs 에 몇번째 값인지 cnt 값 들어 있음
                    com.example.regicard.DATA.ItemDTO oItem = new com.example.regicard.DATA.ItemDTO();
                    oItem.Guestnm = RegicardRestDTO.getGuestnm();
                    oItem.Telno = RegicardRestDTO.getTelno();

                    if (gubun == "R") {
                        oItem.Bcnc_nm = RegicardRestDTO.getBcnc_name();
                    } else {
                        oItem.Bcnc_nm = RegicardRestDTO.getRoomno();
                    }
//                    oItem.onClickListener = this;
                    adapter.addItem(oItem.Guestnm, oItem.Telno, oItem.Bcnc_nm);
                }
            }

            @Override
            public void onFailure(Call<List<RegicardRestDTO>> call, Throwable t) {
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

    private void rstartintent(int guestidx, List<RegicardRestDTO> RegicardRestDTOs) {
//        Intent intent = new Intent(MainActivity.this, RegicardFragment.class);
//        fragment_regicard = new RegistrationCardFragment();
        Bundle bundle = new Bundle();

        for (int i = 0; i < RegicardRestDTOs.size(); i++) {
            if (guestidx == i) {
                bundle.putString("para_cmpny_cd", cmpny_cd);        // 변환된 Srting 값을 parameter key 값으로 Intent 에 Put 시킨다.
                bundle.putString("para_resve_no", RegicardRestDTOs.get(guestidx).Resve_no);
                bundle.putString("para_seq", RegicardRestDTOs.get(guestidx).Seq);
                bundle.putString("para_gubun", "R");
//                bundle.putString("para_proflno", RegicardRestDTOs.get(guestidx).proflno);
                bundle.putString("para_guest_nm", RegicardRestDTOs.get(guestidx).Guest_nm);
                bundle.putString("para_roomno", RegicardRestDTOs.get(guestidx).Roomno);
                bundle.putString("para_bcnc_name", RegicardRestDTOs.get(guestidx).Bcnc_nm);
                bundle.putString("para_country", RegicardRestDTOs.get(guestidx).Country);
                bundle.putString("para_telno", RegicardRestDTOs.get(guestidx).Telno);
                bundle.putString("para_passport", RegicardRestDTOs.get(guestidx).Passport);
                bundle.putString("para_adres", RegicardRestDTOs.get(guestidx).Adres);
                bundle.putString("para_arrdate", RegicardRestDTOs.get(guestidx).Arrdate);
                bundle.putString("para_deptdate", RegicardRestDTOs.get(guestidx).Deptdate);
//                bundle.putString("para_user_name", User_Name);
            }
        }

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction().remove(RegimainFragment.this).commit();
        fragmentManager.popBackStack();

//        //fragment_regicard로 번들 전달
//        fragment_regicard.setArguments(bundle);
//        MainActivity.fragmentStack.push(fragment_regicard);
//        fm = getFragmentManager();
//        tran = fm.beginTransaction();
//        tran.replace(R.id.main_frame, fragment_regicard);
//        tran.addToBackStack(null);
//        tran.commit();
    }

    private void fstartintent(int guestidx, List<RegicardRestDTO> RegicardRestDTOs) {
//        Intent intent = new Intent(MainActivity.this, RegicardFragment.class);
//        fragment_foliomain = new RegistrationCardFragment();
        Bundle bundle = new Bundle();

        for (int i = 0; i < RegicardRestDTOs.size(); i++) {
            if (guestidx == i) {
                bundle.putString("para_cmpny_cd", cmpny_cd);        // 변환된 Srting 값을 parameter key 값으로 Intent 에 Put 시킨다.
                bundle.putString("para_resve_no", RegicardRestDTOs.get(guestidx).Resve_no);
                bundle.putString("para_seq", RegicardRestDTOs.get(guestidx).Seq);
                bundle.putString("para_gubun", "F");
            }
        }

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction().remove(RegimainFragment.this).commit();
        fragmentManager.popBackStack();

        //fragment_regicard로 번들 전달
//        fragment_foliomain.setArguments(bundle);
//        MainActivity.fragmentStack.push(fragment_foliomain);
//        fm = getFragmentManager();
//        tran = fm.beginTransaction();
//        tran.replace(R.id.main_frame, fragment_foliomain);
//        tran.addToBackStack(null);
//        tran.commit();
    }




}

