package com.dimfcompany.aknewsclient.networking.apis

import com.dimfcompany.aknewsclient.base.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiGlobal
{
    @Multipart
    @POST(Constants.Urls.INSERT_FILE)
    suspend fun uploadFile(
            @Part("file_original_name") file_original_name: RequestBody?,
            @Part("file_type") file_type: RequestBody?,
            @Part file: MultipartBody.Part?
    ): Response<ResponseBody>

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>
}