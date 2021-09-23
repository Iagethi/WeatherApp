package com.example.w.whatsweather.data

data class WeatherResult(val coord: Coord?, val weather: List<Weather>?, val base: String?, val main: Main?, val visibility: Number?, val wind: Wind?, val sys: Sys?, val clouds: Clouds?, val dt: Long,  val id: Number?, val name: String?, val cod: Number?)

data class Clouds(val all: Number?)

data class Coord(val lon: Number?, val lat: Number?)

data class Main(val temp: Double, val pressure: Number?, val humidity: Number?, val temp_min: Double, val temp_max: Double)

data class Sys(val type: Number?, val id: Number?, val message: Number?, val country: String?, val sunrise: Long, val sunset: Long)

data class Weather(val id: Number?, val main: String?, val description: String?, val icon: String?)

data class Wind(val speed: Number?, val deg: Number?)
