package com.example.regicard.FRAGMENT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.regicard.DATA.RegicardDTO;
import com.example.regicard.MainActivity;
import com.example.regicard.R;
import com.example.regicard.RETROFIT.RetrofitService;
import com.example.regicard.RETROFIT.ServiceGenerator;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
            textPassportNo, textPurposeofVisit, textRemark, textConsent;

    //editText
    EditText editArrivalDate, editDepartureDate, editRoomNo, editNights, editAdult, editChild, editName, editTel, editAddr, editCarNo, editCompany, editNationality,
            editPassportNo, editPurposeofVisit, editRemark;

    //체크박스
    CheckBox checkConsent;

    //버튼
    Button btnSave;

    //싸인
    SignaturePad sign;
    Bitmap signbitmap;
    String file_name;

    //서버에 보낼 데이터 화면에 없음
    String resno = "";

    //메인
    MainActivity activity;
    Fragment fragment_registration_start;
    Bundle bundle;

    String TAG = "RegistrationCardOkFragment : ";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_registration_card_ok, container, false);

        //전화면에서 받아온 데이터
        bundle = getArguments();
        RegicardDTO item = (RegicardDTO) bundle.getSerializable("item");

//        아이디 찾기 함수
        findId();

//        버전체크
        if (bundle.getString("ver") == "KOR") {
            verKorchange();
        }
        //아이템셋팅
        if( item != null) {
            //setItem(item);
            resno = item.resno;
        }

        //액티비티 초기화
        activity = (MainActivity)getContext();

        //알림창
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        //SAVE 버튼 리스너
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //싸인패드 체크 & 동의 체크
                if (sign.isEmpty()) {
                    alert.setTitle("알림");
                    alert.setMessage("싸인 하여 주시기 바랍니다. ");
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.show();
                    return;
                } else if (checkConsent.isChecked() == false) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("알림");
                    alert.setMessage("동의함에 체크 하여 주시기 바랍니다. ");
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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
                saveData(context, signbitmap, saveDate);
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




        return viewGroup;

    }

    //데이터 셋팅 함수
    private void setItem(RegicardDTO item) {
        editArrivalDate.setText(item.getArrdt());
        editDepartureDate.setText(item.getDepdt());
        editRoomNo.setText(item.getRoomno());
        editNights.setText(item.getNights());
        editAdult.setText(item.getAdult());
        editChild.setText(item.getChild());
        editName.setText(item.getName());
        editTel.setText(item.getTel());
        editCompany.setText(item.getCompanynm());
        editNationality.setText(item.getNation());
        editPassportNo.setText(item.getPassport());
        editRemark.setText(item.getRemark());
    }

    //한글판 버전으로 바꾸기
    public void verKorchange() {
        textArrivalDate.setText("입실일자");
        textDepartureDate.setText("퇴실일자");
        textRoomNo.setText("객실번호");
        textNights.setText("숙박일수");
        textAdult.setText("어른");
        textChild.setText("어린이");
        textName.setText("성명");
        textTel.setText("연락처");
        textAddr.setText("주소");
        textCarNo.setText("차량번호");
        textCompany.setText("회사");
        textNationality.setText("국적");
        textPassportNo.setText("주민등록번호");
        textPurposeofVisit.setText("방문목적");
        textRemark.setText("기타사항");
        textConsent.setText("개인정보의 제공 및 활용에 동의 하십니까?");
        checkConsent.setText("동의");
    }

    //아이디 찾기 함수
    public void findId() {
        //text
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
        editTel = viewGroup.findViewById(R.id.editTel);
        editAddr = viewGroup.findViewById(R.id.editAddr);
        editCarNo = viewGroup.findViewById(R.id.editCarNo);
        editCompany = viewGroup.findViewById(R.id.editCompany);
        editNationality = viewGroup.findViewById(R.id.editNationality);
        editPassportNo = viewGroup.findViewById(R.id.editPassportNo);
        editPurposeofVisit = viewGroup.findViewById(R.id.editPurposeofVisit);
        editRemark = viewGroup.findViewById(R.id.editRemark);
        //체크박스
        checkConsent = viewGroup.findViewById(R.id.checkConsent);
        //버튼
        btnSave = viewGroup.findViewById(R.id.btnSave);

        sign = viewGroup.findViewById(R.id.sign);
    }



    //save 함수
    public void saveData(Context context, Bitmap bitmap, String date) {

        File storage = context.getFilesDir();   //내부저장소 캐시 경로
        String fileName = date + ".jpg";       //저장할 파일 이름
        File tempFile = new File(storage, fileName);    //storage 에 파일 인스턴스를 생성

        HashMap<String, Object> item = new HashMap<>(); // Request put
        item.put("RESNO", resno);
        item.put("ARRDT", editArrivalDate.getText().toString());
        item.put("DEPDT", editDepartureDate.getText().toString());
        item.put("NIGHTS", editNights.getText().toString());
        item.put("ROOMNO", editRoomNo.getText().toString());
        item.put("ADULT", editAdult.getText().toString());
        item.put("CHILD", editChild.getText().toString());
        item.put("GUESTNM", editName.getText().toString());
        item.put("PHONE", editTel.getText().toString());
        item.put("PASSPORT", editPassportNo.getText().toString());
        item.put("NATION", editNationality.getText().toString());
        item.put("COMPANYNM", editCompany.getText().toString());
        item.put("CARNO", editCarNo.getText().toString());
        item.put("REMARK", editRemark.getText().toString());
        item.put("PURPOSE", editPurposeofVisit.getText().toString());
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

                activity.fragmentChange("MAIN", bundle);
                Toast.makeText(getActivity(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
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
