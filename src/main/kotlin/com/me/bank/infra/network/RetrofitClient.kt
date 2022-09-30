package com.me.bank.infra.network

import com.fasterxml.jackson.databind.ObjectMapper
import com.me.bank.domain.provider.CheckInAccountClient
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class RetrofitClient {

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .build()

    private fun getClient(url: String): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapper().findAndRegisterModules()))
        .build()

    @Bean
    @Value("\${provider.checkin-account.url}")
    fun getCheckInAccountProvider(url: String): CheckInAccountClient =
        this.getClient(url)
            .create(CheckInAccountClient::class.java)
}