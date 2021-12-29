package com.example.SMARTFRONTDESK.FRAGMENT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;


import com.example.SMARTFRONTDESK.DATA.RegicardDTO;
import com.example.SMARTFRONTDESK.DATABASE.DBHelper;
import com.example.SMARTFRONTDESK.MainActivity;
import com.example.SMARTFRONTDESK.R;
import com.example.SMARTFRONTDESK.RETROFIT.RetrofitService;
import com.example.SMARTFRONTDESK.RETROFIT.ServiceGenerator;
import com.github.gcacace.signaturepad.views.SignaturePad;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationCardOkFragment<editTe> extends Fragment {

    //view 생성
    ViewGroup viewGroup;

    //TextView
    TextView textArrivalDate, textDepartureDate, textRoomNo, textNights, textAdult, textChild, textName, textTel, textAddr, textCarNo, textCompany, textNationality,
            textPassportNo, textPurposeofVisit, textRemark, textConsent, textView, textBottom1, textBottom2;

    //editText
    EditText editArrivalDate, editDepartureDate, editRoomNo, editNights, editAdult, editChild, editName, editAddr, editCarNo, editCompany,
            editPassportNo, editRemark, editTel1, editTel2, editTel3;


    //체크박스
    CheckBox checkSeminar, checkInvitation, checkConsent;

    //버튼
    Button btnSave;
    //이미지버튼
    ImageButton btnDD, btnAD;

    //싸인
    SignaturePad sign;
    Bitmap signbitmap;
    String file_name;

    //서버에 보낼 데이터 화면에 없음
    String resno = "";

    //메인
    MainActivity activity;
    Bundle bundle;

    String today, tomorrow;  //날짜

    String TAG = "";

    LinearLayout calendar_dialog;
    CalendarView calendarView;

    ScrollView scrollView;

    AlertDialog.Builder alert;


    String check;
    RegicardDTO item;


    //DB
    DBHelper helper;
    SQLiteDatabase db;

    //스피너
    ArrayAdapter AdapterNationality;
    Spinner spinnerNationality; //국적

    //알러트창 문구
    String notice, confirm, calendar, consentCheck, signCheck, dateCheck, checkIn, checkOut, cancel, saveChck, errCheck;

    
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_card_ok, container, false);

        //전화면에서 받아온 데이터
        bundle = getArguments();
        item = (RegicardDTO) bundle.getSerializable("item");

        //아이디 찾기 함수
        findId();

        //alert창 문구
        settting(bundle);


        //디비
        helper = DBHelper.getInstance(getContext()); //디비얻어옴
        db = helper.getWritableDatabase();  //디비 열기

        //액티비티 초기화
        activity = (MainActivity)getContext();

        //바텀 완성
        textDB();

        //스피너 셋팅
        spinnerDB();

        //알러트창 초기화
        alert = new AlertDialog.Builder(getActivity());

        //달력버튼 조회해서 오면 클릭불가 & Warek In 셋팅에서는 true
        btnDD.setEnabled(false);
        btnAD.setEnabled(false);

        //입실일자 & 퇴실일자 & 숙박일수 클릭 금지
        editArrivalDate.setClickable(false);
        editArrivalDate.setFocusable(false);
        editDepartureDate.setClickable(false);
        editDepartureDate.setFocusable(false);
        editNights.setClickable(false);
        editNights.setFocusable(false);


        //버전체크
        if (bundle.getString("ver") == "KOR") {
            verKorchange();
        }

        //아이템셋팅
        if( item != null) {
            setItem(item);
            resno = item.resno;
        }

        //WalkIn 확인
        if ( "NEW" == bundle.getString("check")) {
            try {
                walkIn();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }







        //SAVE 버튼 리스너
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //싸인패드 체크 & 동의 체크
                if (sign.isEmpty()) {
                    //알림창
                    alert.setTitle(notice);
                    alert.setMessage(signCheck);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.show();
                    return;
                    
                } else if (checkConsent.isChecked() == false) { //개인정보제공 동의
                    alert.setTitle(notice);
                    alert.setMessage(consentCheck);
                    alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.show();
                    return;
                } else if (Integer.parseInt(editNights.getText().toString()) < 0) {
                    alert.setTitle(notice);
                    alert.setMessage(dateCheck);
                    alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.show();
                    return;
                }

                long now = System.currentTimeMillis();  //현재시간 가져오기
                Date date = new Date(now);  //date 형식으로 바꿈
                SimpleDateFormat simpledate = new SimpleDateFormat("yyyyMMddhhmmss");    //형식
                String saveDate = simpledate.format(date);

                //데이터 저장
                Context context = getActivity();
                saveData(context, signbitmap, saveDate, check, item);
            }
        });

        //sign 패드 리스너
        sign.setOnSignedListener(new SignaturePad.OnSignedListener(){

            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                signbitmap = sign.getSignatureBitmap();
                file_name =  "R.No_" + textRoomNo;
            }

            @Override
            public void onClear() {

            }
        });

        //체크박스 클릭 리스너
        checkSeminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInvitation.isChecked()){
                    checkInvitation.setChecked(false);
                }
            }
        });
        checkInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSeminar.isChecked()) {
                    checkSeminar.setChecked(false);
                }
            }
        });

        //입실일자 포커스 아웃 리스너
        editArrivalDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {    //포커스 아웃일때
                    try {
                        Date format1 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                        Date format2 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이
                        long diffDays = diffSec / (24*60*60); //일자수 차이
                        editNights.setText(String.valueOf(diffDays));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //퇴실일자 포커스아웃 리스너
        editDepartureDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {    //포커스 아웃일때
                    try {
                        Date format1 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                        Date format2 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이
                        long diffDays = diffSec / (24*60*60); //일자수 차이
                        editNights.setText(String.valueOf(diffDays));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //입실일자 달력버튼
        btnAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar_dialog = (LinearLayout) View.inflate(activity, R.layout.calendar_dialog, null);
                AlertDialog.Builder dig = new AlertDialog.Builder(activity);

                //달력아이디 찾기
                calendarView = calendar_dialog.findViewById(R.id.calendarView);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(editArrivalDate.getText().toString());
                    long cal = date.getTime();
                    calendarView.setDate(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //날짜 클릭이벤트
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        //퇴실일자 날짜 입력
                        String date = year + "-" + (month+1) + "-" + dayOfMonth;
                        editArrivalDate.setText(date);

                        //숙박일 계산
                        try {
                            Date format1 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                            Date format2 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                            long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이
                            long diffDays = diffSec / (24*60*60); //일자수 차이
                            editNights.setText(String.valueOf(diffDays));

                            if(diffDays < 0) {
                                alert.setTitle(notice);
                                alert.setMessage(checkIn);
                                alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });

                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                //오늘
                                today = sdf.format(cal.getTime());
                                editArrivalDate.setText(today);


                                Date format3 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                                Date format4 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                                long diffSec1 = (format3.getTime() - format4.getTime()) / 1000; //초 차이
                                long diffDays1 = diffSec1 / (24*60*60); //일자수 차이
                                editNights.setText(String.valueOf(diffDays1));
                                alert.show();
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dig.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dig.setNegativeButton(cancel, null);
                dig.setTitle(calendar);
                dig.setView(calendar_dialog);
                dig.show();
            }
        });

        //퇴실일자 달력버튼
        btnDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar_dialog = (LinearLayout) View.inflate(activity, R.layout.calendar_dialog, null);
                AlertDialog.Builder dig = new AlertDialog.Builder(activity);

                //달력아이디 찾기
                calendarView = calendar_dialog.findViewById(R.id.calendarView);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(editDepartureDate.getText().toString());
                    long cal = date.getTime();
                    calendarView.setDate(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //날짜 클릭이벤트
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        //퇴실일자 날짜 입력
                        String date = year + "-" + (month+1) + "-" + dayOfMonth;
                        editDepartureDate.setText(date);

                        //숙박일 계산
                        try {
                            Date format1 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                            Date format2 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                            long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이
                            long diffDays = diffSec / (24*60*60); //일자수 차이
                            editNights.setText(String.valueOf(diffDays));

                            if(diffDays < 0) {
                                alert.setTitle(notice);
                                alert.setMessage(checkOut);
                                alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });

                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                //내일날짜
                                cal.add(cal.DATE, +1);
                                tomorrow = sdf.format(cal.getTime());
                                editDepartureDate.setText(tomorrow);


                                Date format3 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                                Date format4 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                                long diffSec1 = (format3.getTime() - format4.getTime()) / 1000; //초 차이
                                long diffDays1 = diffSec1 / (24*60*60); //일자수 차이
                                editNights.setText(String.valueOf(diffDays1));
                                alert.show();
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dig.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dig.setNegativeButton(cancel, null);
                dig.setTitle(calendar);
                dig.setView(calendar_dialog);
                dig.show();
            }
        });

        //2개이상 스크롤뷰가 존재하면 해야함
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

       return viewGroup;

    }

    //알러트창 문구셋팅
    public void settting(Bundle bundle) {
        if(bundle.getString("ver") == "KOR") {
            notice = "알림";
            confirm = "확인";
            calendar = "달력";
            consentCheck = "동의함에 체크 하여 주시기 바랍니다.";
            signCheck = "싸인 하여 주시기 바랍니다.";
            dateCheck = "날짜를 확인 하여주시기 바랍니다.";
            checkIn = "입실일자가 퇴실일자보다 많으면 안됩니다.";
            checkOut = "퇴실일자가 입실일자보다 적으면 안됩니다";
            cancel = "취소";
            saveChck = "저장 성공입니다.";
            errCheck = "서버연결을 실패하였습니다.";
        } else {
            notice = "notice";
            confirm = "confirm";
            calendar = "calendar";
            consentCheck = "Please Check Agree...";
            signCheck = "Signature, please";
            dateCheck = "Please check the date.";
            checkIn = "Please check the check-in date.";
            checkOut = "Please check the check-out date.";
            cancel = "cancel";
            saveChck = "has been saved...!";
            errCheck = "Sever Conneetion error";
        }

    }

    public void textDB() {

        String sql = "select * from COMMON where CODE like 'C%'order by CODE";
        Cursor c = db.rawQuery(sql, null);


        String[] comcd = new String[c.getCount()];
        c.moveToFirst();    //첫번째행 이동
        for(int i=0; i<5; i++) {
            comcd[i] = c.getString(c.getColumnIndex("REMARK"));
            c.moveToNext(); //다음행으로이동
        }
        textBottom1.setText("상호. " + comcd[0] + " / 사업자등록번호. " + comcd[1] + " / Tel. " + comcd[2] + " / " + comcd[3]);
        textBottom2.setText("Add. " + comcd[4]);

    }


    //스피너 셋팅
    public void spinnerDB() {

        String sql = "select * from COMMON where CODE like 'N%'order by CODE";
        Cursor c = db.rawQuery(sql, null);


        String[] nationality = new String[c.getCount()];
        c.moveToFirst();    //첫번째행 이동
        for(int i=0; i<c.getCount(); i++) {
            nationality[i] = c.getString(c.getColumnIndex("REMARK"));
            c.moveToNext(); //다음행으로이동
        }

        AdapterNationality = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, nationality);
        AdapterNationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNationality.setAdapter(AdapterNationality);
    }

    //walk In 셋팅
    public void walkIn() throws ParseException {

        //데이터 받아와서 어떻게할지 설정 프로시저에 데이터가 없음
        checkSeminar.setChecked(true);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //오늘날짜
        today = sdf.format(cal.getTime());
        //내일날짜
        cal.add(cal.DATE, +1);
        tomorrow = sdf.format(cal.getTime());

        //남은 일자 구하기
        Date format1 = new SimpleDateFormat("yyyy-MM-dd").parse(tomorrow);
        Date format2 = new SimpleDateFormat("yyyy-MM-dd").parse(today);
        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이
        long diffDays = diffSec / (24*60*60); //일자수 차이

        editArrivalDate.setText(today);
        editDepartureDate.setText(tomorrow);
        editAdult.setText("1");
        editChild.setText("0");
        editNights.setText(String.valueOf(diffDays));
        btnDD.setEnabled(true);
        btnAD.setEnabled(true);
        check = "new";
    }

    //데이터 셋팅 함수
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setItem(RegicardDTO item) {

        //데이터 받아와서 어떻게할지 설정 프로시저에 데이터가 없음
        checkSeminar.setChecked(true);

        //날짜로 형변환하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);
        LocalDate arrdt = LocalDate.parse(item.getArrdt(), formatter);
        LocalDate depdt = LocalDate.parse(item.getDepdt(), formatter);

        editArrivalDate.setText(arrdt.toString());
        editDepartureDate.setText(depdt.toString());
        editRoomNo.setText(item.getRoomno());
        editNights.setText(item.getNights());
        editAdult.setText(item.getAdult());
        editChild.setText(item.getChild());
        editName.setText(item.getName());
        editCompany.setText(item.getCompanynm());
        editPassportNo.setText(item.getPassport());
        editRemark.setText(item.getRemark());

        //연락처 셋팅
        String[] phone = item.getTel().split("-");
        if(phone.length >= 3 ) {
            editTel1.setText(phone[0]);
            editTel2.setText(phone[1]);
            editTel3.setText(phone[2]);
        }

        //국적셋팅
        if(item.getNation().equals("FOR")){
            spinnerNationality.setSelection(1);
        }

    }

    //한글판 버전으로 바꾸기
    public void verKorchange() {
        textArrivalDate.setText("입실일자");
        textDepartureDate.setText("퇴실일자");
        textRoomNo.setText("객실번호");
        textNights.setText("숙박일수");
        textAdult.setText("어른");
        textChild.setText("어린이");
        textName.setText("고객명");
        textTel.setText("연락처");
        textAddr.setText("주소");
        textCarNo.setText("차량번호");
        textCompany.setText("회사");
        textNationality.setText("국적");
        textPassportNo.setText("고객구분");
        textPurposeofVisit.setText("방문목적");
        textRemark.setText("기타사항");
        textConsent.setText("개인정보의 제공 및 활용에 동의 하십니까?");
        editCarNo.setHint(" 000 가 1234");
        checkSeminar.setText("세미나\n(워크샵)");
        checkInvitation.setText("출판도시\n견학 및 체험");
        checkConsent.setText("동의");



        textView.setText("호텔은 서비스를 제공 하기 위해 아래와 같이 본인의 개인 정보를 수집 하고 있습니다.\n" +
                "필수적인 개인정보의 수집이용에 관한 사항\n" +
                "수집 이용 항목 : 성명, 주소, 전화번호, 국적 \n" +
                "수집 이용 목적 : 본인식별, 서비스 및 정보제공, 분실물, 민원처리 등\n" +
                "보유 이용 기간 : 수집 이용 동의일로 부터(체크인) 3년간\n" +
                "위 사항에 대한 동의를 거부 하실수 있으며, 다만 이경우 투숙및 서비스 제공에 제한이 될 수 있습니다.");

    }

    //아이디 찾기 함수
    public void findId() {
        //text
        textBottom1 = viewGroup.findViewById(R.id.textBottom1);
        textBottom2 = viewGroup.findViewById(R.id.textBottom2);
        textView = viewGroup.findViewById(R.id.textView);
        textArrivalDate = viewGroup.findViewById(R.id.textArrivalDate);
        textDepartureDate = viewGroup.findViewById(R.id.textDepartureDate);
        textRoomNo = viewGroup.findViewById(R.id.textRoomNo);
        textNights = viewGroup.findViewById(R.id.textNights);
        textAdult = viewGroup.findViewById(R.id.textAdult);
        textChild = viewGroup.findViewById(R.id.textChild);
        textName = viewGroup.findViewById(R.id.textName);
        textTel = viewGroup.findViewById(R.id.textTel);
        textAddr = viewGroup.findViewById(R.id.textAddr);
        textCarNo = viewGroup.findViewById(R.id.textCarNo);
        textCompany = viewGroup.findViewById(R.id.textCompanynm);
        textNationality = viewGroup.findViewById(R.id.textNationality);
        textPassportNo = viewGroup.findViewById(R.id.textPassportNo);
        textPurposeofVisit = viewGroup.findViewById(R.id.textPurposeofVisit);
        textRemark = viewGroup.findViewById(R.id.textRemark);
        textConsent = viewGroup.findViewById(R.id.textConsent);
        //editText
        editArrivalDate = viewGroup.findViewById(R.id.editArrivalDate);
        editDepartureDate = viewGroup.findViewById(R.id.editDepartureDate);
        editRoomNo = viewGroup.findViewById(R.id.editRoomNo);
        editNights = viewGroup.findViewById(R.id.editNights);
        editAdult = viewGroup.findViewById(R.id.editAdult);
        editChild = viewGroup.findViewById(R.id.editChild);
        editName = viewGroup.findViewById(R.id.editName);
        editAddr = viewGroup.findViewById(R.id.editAddr);
        editCarNo = viewGroup.findViewById(R.id.editCarNo);
        editCompany = viewGroup.findViewById(R.id.editCompany);
        editPassportNo = viewGroup.findViewById(R.id.editPassportNo);

        editRemark = viewGroup.findViewById(R.id.editRemark);
        editTel1 = viewGroup.findViewById(R.id.editTel1);
        editTel2 = viewGroup.findViewById(R.id.editTel2);
        editTel3 = viewGroup.findViewById(R.id.editTel3);


        //체크박스
        checkSeminar = viewGroup.findViewById(R.id.checkSeminar);
        checkInvitation = viewGroup.findViewById(R.id.checkInvitation);
        checkConsent = viewGroup.findViewById(R.id.checkConsent);
        //버튼
        btnSave = viewGroup.findViewById(R.id.btnSave);
        //이미지버튼
        btnDD = viewGroup.findViewById(R.id.btnDD);
        btnAD = viewGroup.findViewById(R.id.btnAD);
        //싸인
        sign = viewGroup.findViewById(R.id.sign);

        //scrollView
        scrollView = viewGroup.findViewById(R.id.scrollView);

        //국적 스피너
        spinnerNationality = viewGroup.findViewById(R.id.spinnerNationality);
    }

    //save 함수
    public void saveData(Context context, Bitmap bitmap, String date, String check, RegicardDTO dto) {

        File storage = context.getFilesDir();   //내부저장소 캐시 경로
        String fileName = date + ".jpg";       //저장할 파일 이름
        File tempFile = new File(storage, fileName);    //storage 에 파일 인스턴스를 생성

        HashMap<String, Object> item = new HashMap<>(); // Request put
        item.put("RESNO", resno);
        item.put("ARRDT", editArrivalDate.getText().toString().replace("-", ""));
        item.put("DEPDT", editDepartureDate.getText().toString().replace("-", ""));
        item.put("NIGHTS", editNights.getText().toString());
        item.put("ROOMNO", editRoomNo.getText().toString());
        item.put("ADULT", editAdult.getText().toString());
        item.put("CHILD", editChild.getText().toString());
        item.put("GUESTNM", editName.getText().toString());

        if(check == "new") {
            item.put("PHONE", editTel1.getText().toString()+"-"+editTel2.getText().toString()+"-"+editTel3.getText().toString());
        } else {
            if(editTel2.getText().toString().equals("****") || editTel2.getText().toString().equals("***")) {
                item.put("PHONE", dto.getPhone());
            } else {
                item.put("PHONE", editTel1.getText().toString()+"-"+editTel2.getText().toString()+"-"+editTel3.getText().toString());
            }

        }

        item.put("PASSPORT", editPassportNo.getText().toString());
        item.put("NATION", spinnerNationality.getSelectedItem().toString());
        item.put("COMPANYNM", editCompany.getText().toString());
        item.put("CARNO", editCarNo.getText().toString());
        item.put("REMARK", editRemark.getText().toString());

        //방문목적 데이터
        String purpose = "";
        if(checkSeminar.isChecked()) {
            purpose = "seminar";
        } else if(checkInvitation.isChecked()) {
            purpose = "invitation";
        }

        item.put("PURPOSE", purpose);
        item.put("isAGREE",  "1");
        item.put("SIGNKEY", fileName);
        item.put("INSDT", date.substring(0, 8));
        item.put("INSTM", date.substring(8, 14));
        item.put("UPDDT", date.substring(0, 8));
        item.put("UPDTM", date.substring(8, 14));

        //이미지 보낼때 파일명명
        HashMap<String, RequestBody> imgItem = new HashMap<>(); // Request put
        imgItem.put("SIGNKEY", RequestBody.create(MediaType.parse("text/plain"), fileName));

       try {
            tempFile.createNewFile();         // 자동으로 빈 파일을 생성
            FileOutputStream out = new FileOutputStream(tempFile);       // 파일을 쓸 수 있는 스트림
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);       // compress 함수를 사용해 스트림에 비트맵을 저장
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }

        // 이미지 서버 전송
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        File file = new File(tempFile.getPath()); // 이미지파일주소는 확인됨
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multiPartFile = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);

        Call<ResponseBody> request = service.RegicardSaveImg(multiPartFile, imgItem);
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody result = response.body();
                Log.e("Upload",  result.toString());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

        //////////////데이터 전송
        Call req = service.RegicardSave(item);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                //메인화면 넘어가기
                //리턴값 받아서 메인화면으로 갈것

                alert.setTitle(notice);
                alert.setMessage(saveChck);
                alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                AlertDialog dialog = alert.show();       // alert.setMessage 글자 폰트 사이즈 조정
                TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                msgView.setTextSize(20);
                //1초뒤에 닫기버튼 활성화
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    public void run(){
                        dialog.dismiss();
                    }
                },3000);
                activity.fragmentChange("MAIN", bundle);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                alert.setTitle(notice);
                alert.setMessage(errCheck);
                alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
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
