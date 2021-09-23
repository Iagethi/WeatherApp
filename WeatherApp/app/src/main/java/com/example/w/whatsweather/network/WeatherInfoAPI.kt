package com.example.w.whatsweather.network


import com.example.w.whatsweather.data.WeatherInfoResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherInfoAPI {
    @GET("/data/2.5/onecall")
    fun getInfoWeather(@Query("lat") lat: Double?, @Query("lon") lon: Double?,@Query("units") units: String = "metric", @Query("appid") appId: String = "9967704e9e2f29e80c1b517bcbc8614b") : Call<WeatherInfoResult>
}