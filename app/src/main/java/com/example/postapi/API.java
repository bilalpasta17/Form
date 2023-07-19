package com.example.postapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {

    @POST("users")
    Call<ResponseModel>createPost(@Body DataModel dataModel);
}
