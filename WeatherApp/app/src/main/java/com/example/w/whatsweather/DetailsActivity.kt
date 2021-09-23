package com.example.w.whatsweather

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.w.whatsweather.adapter.CitiesListAdapter
import com.example.w.whatsweather.data.WeatherInfoResult
import com.example.w.whatsweather.data.WeatherResult
import com.example.w.whatsweather.network.WeatherAPI
import com.example.w.whatsweather.network.WeatherInfoAPI
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.hourly_details.*
import kotlinx.android.synthetic.main.other_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class DetailsActivity : AppCompatActivity() {

    lateinit var weatherAPI: WeatherAPI
    lateinit var weatherInfoAPI: WeatherInfoAPI
    private val HOST_URL = "https://api.openweathermap.org/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        if (intent.hasExtra(CitiesListAdapter.KEY_DATA)) {
            tvCityName.text = intent.getStringExtra(CitiesListAdapter.KEY_DATA)
        }

        btnHome.setOnClickListener {
            var intentBack = Intent()
            intentBack.setClass(DetailsActivity@this, MainActivity::class.java)
            startActivity(intentBack)
            finish()
        }

        initRetrofit()

        makeAPICall(tvCityName.text.toString())
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        weatherAPI = retrofit.create(WeatherAPI::class.java)
        weatherInfoAPI = retrofit.create(WeatherInfoAPI::class.java)
    }

    fun makeAPICall(city: String) {
        val weatherCall = weatherAPI.getWeather(city)

        weatherCall.enqueue(object : Callback<WeatherResult> {

            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {

                Toast.makeText(this@DetailsActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherResult = response.body()
                if (weatherResult == null) {
                    setTvValuesForNullResponseResult()
                }
                else {
                    resetTvDefaultSettings()
                    setTvValuesWithWeatherData(weatherResult)
                    makeINFOAPICall(weatherResult)
                }
            }
        })
    }

    fun makeINFOAPICall(weatherResult: WeatherResult?) {

        val lat = weatherResult?.coord?.lat?.toDouble()
        val lon = weatherResult?.coord?.lon?.toDouble()

        val weatherInfoCall = weatherInfoAPI.getInfoWeather(lat, lon)

        weatherInfoCall.enqueue(object : Callback<WeatherInfoResult> {

            override fun onFailure(call: Call<WeatherInfoResult>, t: Throwable) {
                Toast.makeText(this@DetailsActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<WeatherInfoResult>, response: Response<WeatherInfoResult>) {
                val weatherInfoResult = response.body()
                if (weatherInfoResult == null) {
                    setTvValuesForNullResponseResult()
                }
                else {
                    resetTvDefaultSettings()
                    setTvValuesWithWeatherData(weatherResult)
                    setOtherSettings(weatherInfoResult)
                }
            }
        })
    }

    private fun setTvValuesWithWeatherData(weatherResult: WeatherResult?) {
        Glide.with(this@DetailsActivity).load("https://openweathermap.org/img/w/" +
                weatherResult?.weather?.get(0)?.icon
                + ".png").into(weatherIcon)

        val dt = weatherResult?.dt
        val dtSunrise = weatherResult?.sys?.sunrise
        val dtSunset = weatherResult?.sys?.sunset
        val temp = Math.round(weatherResult!!.main!!.temp)
        //val tempMax = Math.round(weatherResult.main!!.temp_max)
        //val tempMin = Math.round(weatherResult.main!!.temp_min)

        dayTime.text = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(Date(dt?.times(1000)!!))
        tvTemperature.text = temp.toString() + "°C"
        tvWeatherDescription.text = weatherResult?.weather?.get(0)?.description.toString()
        sunset.text =  SimpleDateFormat("H:mm", Locale.ENGLISH).format(Date(dtSunset!! *1000))
        sunrise.text = SimpleDateFormat("H:mm", Locale.FRENCH).format(Date(dtSunrise!! *1000))
        humidity.text =  weatherResult?.main?.humidity?.toString() + "%"
        pressure.text = weatherResult?.main?.pressure?.toString() + " hPa"
        wind.text = weatherResult?.wind?.speed?.toString() + " Km/h"
        windDirection.text = weatherResult?.wind?.deg?.toString()
    }

    private fun setOtherSettings(weatherInfoResult: WeatherInfoResult) {
        setDailySettings(weatherInfoResult)
        setHourlySettings(weatherInfoResult)
    }

    private fun setDailySettings (weatherInfoResult: WeatherInfoResult) {
        for (i in 0..5) {
            val dt = weatherInfoResult.daily.get(i).dt.toLong()
            val dailyDesc = weatherInfoResult.daily.get(i).weather.get(0).description
            val dailyTempMin = Math.round(weatherInfoResult.daily.get(i).temp.min)
            val dailyTempMax = Math.round(weatherInfoResult.daily.get(i).temp.max)
            val dailyTemperature = dailyTempMin.toString() + "/" + dailyTempMax.toString() +"°C"

            when (i) {
                0 -> dailyDate.text = "Today"
                1 -> dailyDate1.text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                2 -> dailyDate2.text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                3 -> dailyDate3.text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                4 -> dailyDate4.text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                5 -> dailyDate5.text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
            }

            when (i) {
                0 -> dailyStatus.text = dailyDesc
                1 -> dailyStatus1.text = dailyDesc
                2 -> dailyStatus2.text = dailyDesc
                3 -> dailyStatus3.text = dailyDesc
                4 -> dailyStatus4.text = dailyDesc
                5 -> dailyStatus5.text = dailyDesc
            }

            when (i) {
                0 -> dailyTemp.text = dailyTemperature
                1 -> dailyTemp1.text = dailyTemperature
                2 -> dailyTemp2.text = dailyTemperature
                3 -> dailyTemp3.text = dailyTemperature
                4 -> dailyTemp4.text = dailyTemperature
                5 -> dailyTemp5.text = dailyTemperature
            }
        }
    }

    private fun setHourlySettings(weatherInfoResult: WeatherInfoResult) {
        for (i in 0..12) {

            val hourlyTemp = Math.round(weatherInfoResult.hourly.get(i).temp)
            val hourlyTimeUnix = weatherInfoResult.hourly.get(i).dt
            val hourly = hourlyTemp.toString() + "°C"
            val hourlyTime = SimpleDateFormat("H", Locale.FRANCE).format(Date(hourlyTimeUnix*1000)) + ":00"

            when (i) {
                0 -> tempHourly.text = hourly
                1 -> tempHourly1.text = hourly
                2 -> tempHourly2.text = hourly
                3 -> tempHourly3.text = hourly
                4 -> tempHourly4.text = hourly
                5 -> tempHourly5.text = hourly
                6 -> tempHourly6.text = hourly
                7 -> tempHourly7.text = hourly
                8 -> tempHourly8.text = hourly
                9 -> tempHourly9.text = hourly
                10 -> tempHourly10.text = hourly
                11 -> tempHourly11.text = hourly
                12 -> tempHourly12.text = hourly
            }

            when (i) {
                0 -> timeHourly.text = hourlyTime
                1 -> timeHourly1.text = hourlyTime
                2 -> timeHourly2.text = hourlyTime
                3 -> timeHourly3.text = hourlyTime
                4 -> timeHourly4.text = hourlyTime
                5 -> timeHourly5.text = hourlyTime
                6 -> timeHourly6.text = hourlyTime
                7 -> timeHourly7.text = hourlyTime
                8 -> timeHourly8.text = hourlyTime
                9 -> timeHourly9.text = hourlyTime
                10 -> timeHourly10.text = hourlyTime
                11 -> timeHourly11.text = hourlyTime
                12 -> timeHourly12.text = hourlyTime
            }
        }

    }

    private fun resetTvDefaultSettings() {
        tvTemperature.textSize = 40F
        tvWeatherDescription.textSize = 25F
        weatherIcon.visibility = View.VISIBLE
        pressure.visibility = View.VISIBLE
        humidity.visibility = View.VISIBLE
        dailyContainer.visibility = View.VISIBLE
        hourlyLayout.visibility = View.VISIBLE
    }

    private fun setTvValuesForNullResponseResult() {
        tvTemperature.text = getString(R.string.no_info_available)
        tvTemperature.textSize = 25F
        tvWeatherDescription.text = getString(R.string.did_you_misspell)
        tvWeatherDescription.textSize = 20F
        weatherIcon.visibility = View.GONE
        pressure.visibility = View.GONE
        humidity.visibility = View.GONE
        dailyContainer.visibility = View.GONE
        hourlyLayout.visibility = View.GONE

    }
}
