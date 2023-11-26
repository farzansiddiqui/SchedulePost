package com.siddiqui.schedulepost.retrofit

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitApi {
    @POST("{page-id}/feed")
    @FormUrlEncoded
    suspend fun publishedPost(@Path("page-id")pageId:String,
        @Field("message") message:String,@Header("Authorization")accessToken:String):Response<PostData>

}