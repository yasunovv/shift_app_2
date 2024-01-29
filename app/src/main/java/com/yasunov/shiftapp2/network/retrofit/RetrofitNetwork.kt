package com.yasunov.shiftapp2.network.retrofit

import com.yasunov.shiftapp2.network.NetworkDataSource
import com.yasunov.shiftapp2.network.data.Results
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject

private interface RetrofitNetworkApi {
    @GET("?results=50&inc=name,location,picture,phone,login,email,dob&noinfo&format=json")
    suspend fun getUsers(): Results
}

class RetrofitNetwork @Inject constructor(): NetworkDataSource {
    private val networkApi =  Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://randomuser.me/api/1.4/")
        .build()
        .create(RetrofitNetworkApi::class.java)
    override suspend fun getUsers(): Results {
        return networkApi.getUsers()
    }
}