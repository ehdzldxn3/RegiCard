package com.example.regicard.FRAGMENT;

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
import android.widget.AdapterView;
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


import com.example.regicard.DATA.CommonDTO;
import com.example.regicard.DATA.RegicardDTO;
import com.example.regicard.DATABASE.DBHelper;
import com.example.regicard.MainActivity;
import com.example.regicard.R;
import com.example.regicard.RETROFIT.RetrofitService;
import com.example.regicard.RETROFIT.ServiceGenerator;
import com.github.gcacace.signaturepad.views.SignaturePad;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationCardOkFragment<editTe> extends Fragment {

    //view ??????
    ViewGroup viewGroup;

    //TextView
    TextView textArrivalDate, textDepartureDate, textRoomNo, textNights, textAdult, textChild, textName, textTel, textAddr, textCarNo, textCompany, textNationality,
            textPassportNo, textPurposeofVisit, textRemark, textConsent, textView, textBottom1, textBottom2;

    //editText
    EditText editArrivalDate, editDepartureDate, editRoomNo, editNights, editAdult, editChild, editName, editAddr, editCarNo, editCompany,
            editPassportNo, editRemark, editTel1, editTel2, editTel3;


    //????????????
    CheckBox checkSeminar, checkInvitation, checkConsent;

    //??????
    Button btnSave;
    //???????????????
    ImageButton btnDD, btnAD;

    //??????
    SignaturePad sign;
    Bitmap signbitmap;
    String file_name;

    //????????? ?????? ????????? ????????? ??????
    String resno = "";

    //??????
    MainActivity activity;
    Bundle bundle;

    String today, tomorrow;  //??????

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

    //?????????
    ArrayAdapter AdapterNationality;
    Spinner spinnerNationality; //??????

    //???????????? ??????
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

        //??????????????? ????????? ?????????
        bundle = getArguments();
        item = (RegicardDTO) bundle.getSerializable("item");

        //????????? ?????? ??????
        findId();

        //alert??? ??????
        settting(bundle);


        //??????
        helper = DBHelper.getInstance(getContext()); //???????????????
        db = helper.getWritableDatabase();  //?????? ??????

        //???????????? ?????????
        activity = (MainActivity)getContext();

        //?????? ??????
        textDB();

        //????????? ??????
        spinnerDB();

        //???????????? ?????????
        alert = new AlertDialog.Builder(getActivity());

        //???????????? ???????????? ?????? ???????????? & Warek In ??????????????? true
        btnDD.setEnabled(false);
        btnAD.setEnabled(false);

        //???????????? & ???????????? & ???????????? ?????? ??????
        editArrivalDate.setClickable(false);
        editArrivalDate.setFocusable(false);
        editDepartureDate.setClickable(false);
        editDepartureDate.setFocusable(false);
        editNights.setClickable(false);
        editNights.setFocusable(false);


        //????????????
        if (bundle.getString("ver") == "KOR") {
            verKorchange();
        }

        //???????????????
        if( item != null) {
            setItem(item);
            resno = item.resno;
        }

        //WalkIn ??????
        if ( "NEW" == bundle.getString("check")) {
            try {
                walkIn();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }







        //SAVE ?????? ?????????
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //???????????? ?????? & ?????? ??????
                if (sign.isEmpty()) {
                    //?????????
                    alert.setTitle(notice);
                    alert.setMessage(signCheck);
                    alert.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //??????
                        }
                    });
                    alert.show();
                    return;
                    
                } else if (checkConsent.isChecked() == false) { //?????????????????? ??????
                    alert.setTitle(notice);
                    alert.setMessage(consentCheck);
                    alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //??????
                        }
                    });
                    alert.show();
                    return;
                } else if (Integer.parseInt(editNights.getText().toString()) < 0) {
                    alert.setTitle(notice);
                    alert.setMessage(dateCheck);
                    alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //??????
                        }
                    });
                    alert.show();
                    return;
                }

                long now = System.currentTimeMillis();  //???????????? ????????????
                Date date = new Date(now);  //date ???????????? ??????
                SimpleDateFormat simpledate = new SimpleDateFormat("yyyyMMddhhmmss");    //??????
                String saveDate = simpledate.format(date);

                //????????? ??????
                Context context = getActivity();
                saveData(context, signbitmap, saveDate, check, item);
            }
        });

        //sign ?????? ?????????
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

        //???????????? ?????? ?????????
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

        //???????????? ????????? ?????? ?????????
        editArrivalDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {    //????????? ????????????
                    try {
                        Date format1 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                        Date format2 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //??? ??????
                        long diffDays = diffSec / (24*60*60); //????????? ??????
                        editNights.setText(String.valueOf(diffDays));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //???????????? ??????????????? ?????????
        editDepartureDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {    //????????? ????????????
                    try {
                        Date format1 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                        Date format2 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //??? ??????
                        long diffDays = diffSec / (24*60*60); //????????? ??????
                        editNights.setText(String.valueOf(diffDays));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //???????????? ????????????
        btnAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar_dialog = (LinearLayout) View.inflate(activity, R.layout.calendar_dialog, null);
                AlertDialog.Builder dig = new AlertDialog.Builder(activity);

                //??????????????? ??????
                calendarView = calendar_dialog.findViewById(R.id.calendarView);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(editArrivalDate.getText().toString());
                    long cal = date.getTime();
                    calendarView.setDate(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //?????? ???????????????
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        //???????????? ?????? ??????
                        String date = year + "-" + (month+1) + "-" + dayOfMonth;
                        editArrivalDate.setText(date);

                        //????????? ??????
                        try {
                            Date format1 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                            Date format2 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                            long diffSec = (format1.getTime() - format2.getTime()) / 1000; //??? ??????
                            long diffDays = diffSec / (24*60*60); //????????? ??????
                            editNights.setText(String.valueOf(diffDays));

                            if(diffDays < 0) {
                                alert.setTitle(notice);
                                alert.setMessage(checkIn);
                                alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //??????
                                    }
                                });

                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                //??????
                                today = sdf.format(cal.getTime());
                                editArrivalDate.setText(today);


                                Date format3 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                                Date format4 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                                long diffSec1 = (format3.getTime() - format4.getTime()) / 1000; //??? ??????
                                long diffDays1 = diffSec1 / (24*60*60); //????????? ??????
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

        //???????????? ????????????
        btnDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar_dialog = (LinearLayout) View.inflate(activity, R.layout.calendar_dialog, null);
                AlertDialog.Builder dig = new AlertDialog.Builder(activity);

                //??????????????? ??????
                calendarView = calendar_dialog.findViewById(R.id.calendarView);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(editDepartureDate.getText().toString());
                    long cal = date.getTime();
                    calendarView.setDate(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //?????? ???????????????
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        //???????????? ?????? ??????
                        String date = year + "-" + (month+1) + "-" + dayOfMonth;
                        editDepartureDate.setText(date);

                        //????????? ??????
                        try {
                            Date format1 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                            Date format2 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                            long diffSec = (format1.getTime() - format2.getTime()) / 1000; //??? ??????
                            long diffDays = diffSec / (24*60*60); //????????? ??????
                            editNights.setText(String.valueOf(diffDays));

                            if(diffDays < 0) {
                                alert.setTitle(notice);
                                alert.setMessage(checkOut);
                                alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //??????
                                    }
                                });

                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                //????????????
                                cal.add(cal.DATE, +1);
                                tomorrow = sdf.format(cal.getTime());
                                editDepartureDate.setText(tomorrow);


                                Date format3 = new SimpleDateFormat("yyyyMMdd").parse(editDepartureDate.getText().toString().replace("-",""));
                                Date format4 = new SimpleDateFormat("yyyyMMdd").parse(editArrivalDate.getText().toString().replace("-",""));
                                long diffSec1 = (format3.getTime() - format4.getTime()) / 1000; //??? ??????
                                long diffDays1 = diffSec1 / (24*60*60); //????????? ??????
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

        //2????????? ??????????????? ???????????? ?????????
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

       return viewGroup;

    }

    //???????????? ????????????
    public void settting(Bundle bundle) {
        if(bundle.getString("ver") == "KOR") {
            notice = "??????";
            confirm = "??????";
            calendar = "??????";
            consentCheck = "???????????? ?????? ?????? ????????? ????????????.";
            signCheck = "?????? ?????? ????????? ????????????.";
            dateCheck = "????????? ?????? ??????????????? ????????????.";
            checkIn = "??????????????? ?????????????????? ????????? ????????????.";
            checkOut = "??????????????? ?????????????????? ????????? ????????????";
            cancel = "??????";
            saveChck = "?????? ???????????????.";
            errCheck = "??????????????? ?????????????????????.";
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
        c.moveToFirst();    //???????????? ??????
        for(int i=0; i<5; i++) {
            comcd[i] = c.getString(c.getColumnIndex("REMARK"));
            c.moveToNext(); //?????????????????????
        }
        textBottom1.setText("??????. " + comcd[0] + " / ?????????????????????. " + comcd[1] + " / Tel. " + comcd[2] + " / " + comcd[3]);
        textBottom2.setText("Add. " + comcd[4]);

    }


    //????????? ??????
    public void spinnerDB() {

        String sql = "select * from COMMON where CODE like 'N%'order by CODE";
        Cursor c = db.rawQuery(sql, null);


        String[] nationality = new String[c.getCount()];
        c.moveToFirst();    //???????????? ??????
        for(int i=0; i<c.getCount(); i++) {
            nationality[i] = c.getString(c.getColumnIndex("REMARK"));
            c.moveToNext(); //?????????????????????
        }

        AdapterNationality = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, nationality);
        AdapterNationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNationality.setAdapter(AdapterNationality);
    }

    //walk In ??????
    public void walkIn() throws ParseException {

        //????????? ???????????? ??????????????? ?????? ??????????????? ???????????? ??????
        checkSeminar.setChecked(true);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //????????????
        today = sdf.format(cal.getTime());
        //????????????
        cal.add(cal.DATE, +1);
        tomorrow = sdf.format(cal.getTime());

        //?????? ?????? ?????????
        Date format1 = new SimpleDateFormat("yyyy-MM-dd").parse(tomorrow);
        Date format2 = new SimpleDateFormat("yyyy-MM-dd").parse(today);
        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //??? ??????
        long diffDays = diffSec / (24*60*60); //????????? ??????

        editArrivalDate.setText(today);
        editDepartureDate.setText(tomorrow);
        editAdult.setText("1");
        editChild.setText("0");
        editNights.setText(String.valueOf(diffDays));
        btnDD.setEnabled(true);
        btnAD.setEnabled(true);
        check = "new";
    }

    //????????? ?????? ??????
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setItem(RegicardDTO item) {

        //????????? ???????????? ??????????????? ?????? ??????????????? ???????????? ??????
        checkSeminar.setChecked(true);

        //????????? ???????????????
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

        //????????? ??????
        String[] phone = item.getTel().split("-");
        if(phone.length >= 3 ) {
            editTel1.setText(phone[0]);
            editTel2.setText(phone[1]);
            editTel3.setText(phone[2]);
        }

        //????????????
        if(item.getNation().equals("FOR")){
            spinnerNationality.setSelection(1);
        }

    }

    //????????? ???????????? ?????????
    public void verKorchange() {
        textArrivalDate.setText("????????????");
        textDepartureDate.setText("????????????");
        textRoomNo.setText("????????????");
        textNights.setText("????????????");
        textAdult.setText("??????");
        textChild.setText("?????????");
        textName.setText("?????????");
        textTel.setText("?????????");
        textAddr.setText("??????");
        textCarNo.setText("????????????");
        textCompany.setText("??????");
        textNationality.setText("??????");
        textPassportNo.setText("????????????");
        textPurposeofVisit.setText("????????????");
        textRemark.setText("????????????");
        textConsent.setText("??????????????? ?????? ??? ????????? ?????? ?????????????");
        editCarNo.setHint(" 000 ??? 1234");
        checkSeminar.setText("?????????\n(?????????)");
        checkInvitation.setText("????????????\n?????? ??? ??????");
        checkConsent.setText("??????");



        textView.setText("????????? ???????????? ?????? ?????? ?????? ????????? ?????? ????????? ?????? ????????? ?????? ?????? ????????????.\n" +
                "???????????? ??????????????? ??????????????? ?????? ??????\n" +
                "?????? ?????? ?????? : ??????, ??????, ????????????, ?????? \n" +
                "?????? ?????? ?????? : ????????????, ????????? ??? ????????????, ?????????, ???????????? ???\n" +
                "?????? ?????? ?????? : ?????? ?????? ???????????? ??????(?????????) 3??????\n" +
                "??? ????????? ?????? ????????? ?????? ????????? ?????????, ?????? ????????? ????????? ????????? ????????? ????????? ??? ??? ????????????.");

    }

    //????????? ?????? ??????
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


        //????????????
        checkSeminar = viewGroup.findViewById(R.id.checkSeminar);
        checkInvitation = viewGroup.findViewById(R.id.checkInvitation);
        checkConsent = viewGroup.findViewById(R.id.checkConsent);
        //??????
        btnSave = viewGroup.findViewById(R.id.btnSave);
        //???????????????
        btnDD = viewGroup.findViewById(R.id.btnDD);
        btnAD = viewGroup.findViewById(R.id.btnAD);
        //??????
        sign = viewGroup.findViewById(R.id.sign);

        //scrollView
        scrollView = viewGroup.findViewById(R.id.scrollView);

        //?????? ?????????
        spinnerNationality = viewGroup.findViewById(R.id.spinnerNationality);
    }

    //save ??????
    public void saveData(Context context, Bitmap bitmap, String date, String check, RegicardDTO dto) {

        File storage = context.getFilesDir();   //??????????????? ?????? ??????
        String fileName = date + ".jpg";       //????????? ?????? ??????
        File tempFile = new File(storage, fileName);    //storage ??? ?????? ??????????????? ??????

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

        //???????????? ?????????
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

        //????????? ????????? ????????????
        HashMap<String, RequestBody> imgItem = new HashMap<>(); // Request put
        imgItem.put("SIGNKEY", RequestBody.create(MediaType.parse("text/plain"), fileName));

       try {
            tempFile.createNewFile();         // ???????????? ??? ????????? ??????
            FileOutputStream out = new FileOutputStream(tempFile);       // ????????? ??? ??? ?????? ?????????
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);       // compress ????????? ????????? ???????????? ???????????? ??????
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }

        // ????????? ?????? ??????
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        File file = new File(tempFile.getPath()); // ???????????????????????? ?????????
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

        //////////////????????? ??????
        Call req = service.RegicardSave(item);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                //???????????? ????????????
                //????????? ????????? ?????????????????? ??????

                alert.setTitle(notice);
                alert.setMessage(saveChck);
                alert.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //??????
                    }
                });
                AlertDialog dialog = alert.show();       // alert.setMessage ?????? ?????? ????????? ??????
                TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                msgView.setTextSize(20);
                //1????????? ???????????? ?????????
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
                        dialog.dismiss();     //??????
                    }
                });
                AlertDialog dialog = alert.show();       // alert.setMessage ?????? ?????? ????????? ??????
                TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                msgView.setTextSize(20);

            }
        });

    }
}
