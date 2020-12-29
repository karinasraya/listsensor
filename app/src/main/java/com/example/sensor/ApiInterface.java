package com.example.sensor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("list")
    Call<GetData> listPhoto();

    @GET("show/{filename}")
    Call<PostPutDel> showPhoto(@Path("filename") String filename);

    @FormUrlEncoded
    @POST("store")
    Call<PostPutDel> storePhoto(@Field("name") String name,
                                     @Field("filename") String filename,
                                     @Field("photofile") String photofile,
                                     @Field("brightness") double brightness);
}