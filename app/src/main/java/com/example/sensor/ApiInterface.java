package com.example.sensor;

import com.opencsv.CSVWriter;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    @Multipart
    @POST("proses")
    Call<PostPutDel> prosesFile(@Part MultipartBody.Part writer,
                                @Part("keterangan") RequestBody keterangan);
}
