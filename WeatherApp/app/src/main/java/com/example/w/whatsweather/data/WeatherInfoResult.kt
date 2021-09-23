package com.example.w.whatsweather.data

data class WeatherInfoResult(val lat: Number?, val lon: Number?, val timezone: String?, val hourly: List<Hourly>, val daily: List<Daily>)

data class Daily(val dt: Number, val temp: TempDaily, val weather: List<WeatherDaily>)

data class TempDaily(val max: Double, val min: Double)

data class WeatherDaily(val description: String)

data class Hourly(val dt: Long, val temp: Double)
