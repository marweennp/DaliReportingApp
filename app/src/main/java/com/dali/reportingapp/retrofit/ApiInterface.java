package com.dali.reportingapp.retrofit;

import com.dali.reportingapp.models.Response;
import com.dali.reportingapp.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("android_api/Login.php")
    Call<User> userLogIn(@Field("host") String host,
                         @Field("dbname") String dbname,
                         @Field("username") String username,
                         @Field("password") String password,
                         @Field("user_email") String user_email,
                         @Field("user_password") String user_password);

    @FormUrlEncoded
    @POST("android_api/SgineUp.php")
    Call<Response> userSgineUp(@Field("host") String host,
                               @Field("dbname") String dbname,
                               @Field("username") String username,
                               @Field("password") String password,
                               @Field("user_email") String user_email,
                               @Field("user_password") String user_password,
                               @Field("user_first_name") String user_first_name,
                               @Field("user_last_name") String user_last_name);

}
