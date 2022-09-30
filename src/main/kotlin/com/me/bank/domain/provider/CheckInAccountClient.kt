package com.me.bank.domain.provider

import com.me.bank.domain.provider.model.OperationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckInAccountClient {

    @POST("/balance/branch/{branch}/number/{number}")
    fun balance(@Path("branch") branch: String,
                @Path("number") number: String,
                @Body operation: OperationRequest): Call<Void>
}