package com.example.sensor;

import com.opencsv.CSVWriter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("proses")
    Call<PostPutDel> prosesFile(@Field("file") CSVWriter writer,
                                @Field("keterangan") String keterangan);
}
