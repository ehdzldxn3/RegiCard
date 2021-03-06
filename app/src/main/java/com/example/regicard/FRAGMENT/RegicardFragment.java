package com.example.regicard.FRAGMENT;

import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.regicard.DATA.RegicardRestupdDTO;
import com.example.regicard.MainActivity;
import com.example.regicard.R;
import com.example.regicard.RETROFIT.RetrofitService;
import com.example.regicard.RETROFIT.ServiceGenerator;
import com.github.gcacace.signaturepad.views.SignaturePad;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.FragmentManager;



 public class RegicardFragment extends Fragment {
    ViewGroup viewGroup;
    View view;
    TextView guestname, roomno, company, nationality, telno, passport, address, check_in, check_out, username;
    Button save_bt;
    SignaturePad signaturePad;
    String para_cmpny_cd, para_resve_no, para_seq, para_proflno ;
    String file_name;
    Bitmap signbitmap;

//    MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_regicard, container, false);
        guestname = viewGroup.findViewById(R.id.guestname);
        roomno = viewGroup.findViewById(R.id.roomno);
        company = viewGroup.findViewById(R.id.company);
        nationality = viewGroup.findViewById(R.id.nationality);
        telno = viewGroup.findViewById(R.id.telno);
        passport = viewGroup.findViewById(R.id.passport);
        address = viewGroup.findViewById(R.id.address);
        check_in = viewGroup.findViewById(R.id.check_in);
        check_out = viewGroup.findViewById(R.id.check_out);
        username = viewGroup.findViewById(R.id.hotelname);

        Bundle bundle = getArguments();                             //RequestActivity?????? ????????? ?????? ??? get ??????.

        //?????? ?????? ????????? ????????????
        para_cmpny_cd = bundle.getString("para_cmpny_cd");     // ???????????? ????????? ???????????? ?????????.
        para_resve_no = bundle.getString("para_resve_no");
        para_seq = bundle.getString("para_seq");
        para_proflno = bundle.getString("para_proflno");
        String para_guest_nm = bundle.getString("para_guest_nm");
        String para_roomno = bundle.getString("para_roomno");
        String para_adres = bundle.getString("para_adres");
        String para_bcna_name = bundle.getString("para_bcnc_name");
        String para_country = bundle.getString("para_country");
        String para_telno = bundle.getString("para_telno");
        String para_passport = bundle.getString("para_passport");
        String para_deptdate = bundle.getString("para_deptdate");
        String para_arrdate = bundle.getString("para_arrdate");
        String para_username = bundle.getString("para_user_name");

        //fragment1??? TextView??? ?????? ?????? ??? ?????????
        guestname.setText(para_guest_nm);
        roomno.setText(para_roomno);
        address.setText(para_adres);
        company.setText(para_bcna_name);
        nationality.setText(para_country);
        telno.setText(para_telno);
        passport.setText(para_passport);
        check_in.setText(para_arrdate);
        check_out.setText(para_deptdate);
        username.setText(para_username);


        // Sign pad Event ??????
        signaturePad = (SignaturePad)viewGroup.findViewById(R.id.signPad);
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }
            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                signbitmap = signaturePad.getSignatureBitmap();
                file_name =  "R.No_" + para_resve_no + para_seq;
            }
            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }

        });


        final CheckBox cb1 = (CheckBox)viewGroup.findViewById(R.id.check1);

        save_bt = (Button)viewGroup.findViewById(R.id.save_bt);
        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (signaturePad.isEmpty()) {
                 AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                 alert.setTitle("??????");
                 alert.setMessage("?????? ?????? ????????? ????????????. ");
                 alert.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();     //??????
                    }
                    });
                 alert.show();
             }

            if (cb1.isChecked() == false) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("??????");
                alert.setMessage("???????????? ?????? ?????? ????????? ????????????. ");
                alert.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //??????
                    }
                });
                alert.show();
            }


            if ((cb1.isChecked() == true) && (signaturePad.isEmpty() == false)) {

                //  TB_RREGICARD_CON ?????????
                RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
                HashMap<String, Object> input = new HashMap<>(); // Request put
                input.put("cmpny_cd", para_cmpny_cd);
                input.put("resve_no", para_resve_no);
                input.put("seq", para_seq);
                input.put("isSIGN", 'Y');
                input.put("isAGREE", 'Y');

                Call<List<RegicardRestupdDTO>> request = service.Regicard_upd(input);         //  post ??????, List??? ?????????.
                request.enqueue(new Callback<List<RegicardRestupdDTO>>() {
                     @Override
                     public void onResponse(Call<List<RegicardRestupdDTO>> call, Response<List<RegicardRestupdDTO>> response) {
                        if (!response.isSuccessful()) {
//                            test.setText("code: " + response.code());
                          return;
                         }

                        final List<RegicardRestupdDTO> RegicardRestupdDTOs = response.body();

                    if (RegicardRestupdDTOs.size() > 0) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("NOTICE");
                            alert.setMessage("REGISTRATION CARD SAVED !!");
                            alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //??????
                            MainActivity activity = (MainActivity) getActivity();
                            }
                        });

                        AlertDialog dialog = alert.show();
                        TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                        msgView.setTextSize(20);

                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        fragmentManager.beginTransaction().remove(RegicardFragment.this).commit();
                        fragmentManager.popBackStack();


                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("NOTICE");
                        alert.setMessage("\n" +
                                "An error occurred while saving (????????? ????????? ?????? ???????????????.) ");
                        alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();     //??????
                                MainActivity activity = (MainActivity) getActivity();
                            }
                        });

                        AlertDialog dialog = alert.show();
                        TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                        msgView.setTextSize(20);

                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        fragmentManager.beginTransaction().remove(RegicardFragment.this).commit();
                        fragmentManager.popBackStack();

                    }
                }

                @Override
                public void onFailure(Call<List<RegicardRestupdDTO>> call, Throwable t) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("NOTICE");
                    alert.setMessage("????????? ????????? ????????? ?????? ???????????????. ");
                    alert.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //??????
                            MainActivity activity = (MainActivity) getActivity();
                        }
                    });
                }
            });
            Context context = getActivity();
            saveBitmapToJpeg(context, signbitmap, file_name );
        }
     }           // onclick End

    }) ;          // setOnSignedListener End

    return viewGroup;

   }               // onCreate End


    public static void saveBitmapToJpeg(Context context, Bitmap bitmap, String name) {
        File storage = context.getFilesDir();   //??????????????? ?????? ??????
        String fileName = name + ".jpg";       //????????? ?????? ??????
        File tempFile = new File(storage, fileName);    //storage ??? ?????? ??????????????? ??????

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
        HashMap<String, Object> input = new HashMap<>(); // Request put
        Call<ResponseBody> request = service.ImageUpload(multiPartFile);
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body1 = response.toString();
                Log.e("Upload",  body1);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
        // ????????? ?????? ??????
    }

}

