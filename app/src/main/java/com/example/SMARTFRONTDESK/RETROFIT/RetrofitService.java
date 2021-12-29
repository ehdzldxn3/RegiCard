package com.example.SMARTFRONTDESK.RETROFIT;

import com.example.SMARTFRONTDESK.DATA.FolioDTO;
import com.example.SMARTFRONTDESK.DATA.RegicardDTO;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitService {

    //강한별////////////////////////////////////////////////////////////////////////////////////

    //Regicard 조회
    @GET("/SMART/Rest/getguest")
    Call<List<RegicardDTO>> Regicard(
            @Query("res") String res,
            @Query("phone") String phone,
            @Query("name") String name
    );

    //Regicard img 저장
    @Multipart
    @POST("/SMART/Rest/imgUpload")
    Call<ResponseBody> RegicardSaveImg(@Part MultipartBody.Part file,
                                       @PartMap HashMap<String, RequestBody> param

        );

    //Regicard 저장
    @FormUrlEncoded
    @POST("/SMART/Rest/setregicard")
    Call<ResponseBody> RegicardSave(@FieldMap HashMap<String, Object> param);

    //folio 조회
    @GET("/SMART/Rest/getfolio")
    Call<List<FolioDTO>> Folio(@Query("roomno") String roomno);



}
