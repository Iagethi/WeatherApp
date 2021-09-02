package com.example.weatherapp.api

import com.example.weatherapp.model.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
    }

    @GET("weather")
    suspend fun getResult(
        @Query("q") countryName: String?,
        @Query("appId") appId: String
    ): Response<CurrentWeatherResponse?>
}