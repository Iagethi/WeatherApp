package com.example.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String = "London"
    val API: String = "9967704e9e2f29e80c1b517bcbc8614b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WeatherTask().execute()
    }

    inner class WeatherTask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?

            try {
                response = URL("https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&units=metric&appid=$API")
                        //response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                        .readText(Charsets.UTF_8)
            }
            catch (e: Exception)
            {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val current = jsonObj.getJSONObject("current")
                val address = jsonObj.getString("timezone")
                val daily = jsonObj.getJSONArray("daily")
                val hourly = jsonObj.getJSONArray("hourly")

                val jsonObject = JSONTokener(result).nextValue() as JSONObject

                val jsonArrayDay = jsonObject.getJSONArray("daily")
                val jsonArrayHourly = jsonObject.getJSONArray("hourly")

                for (i in 0 until jsonArrayDay.length()) {
                    val dailyTemp = jsonArrayDay.getJSONObject(i).getJSONObject("temp")
                    val dt = jsonArrayDay.getJSONObject(i).getLong("dt")
                    val dailyWeatherStatus = jsonArrayDay.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
                    val dailyStatus = dailyWeatherStatus.getString("description")
                    val tempMin = dailyTemp.getString("min") + " / "
                    val tempMax = dailyTemp.getString("max") + "°C"

                    when (i) {
                        0 -> findViewById<TextView>(R.id.dailyDate).text = "Today"
                        1 -> findViewById<TextView>(R.id.dailyDate1).text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                        2 -> findViewById<TextView>(R.id.dailyDate2).text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                        3 -> findViewById<TextView>(R.id.dailyDate3).text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                        4 -> findViewById<TextView>(R.id.dailyDate4).text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                        5 -> findViewById<TextView>(R.id.dailyDate5).text = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(dt*1000))
                    }


                    when (i) {
                        0 -> findViewById<TextView>(R.id.dailyStatus).text = dailyStatus
                        1 -> findViewById<TextView>(R.id.dailyStatus1).text = dailyStatus
                        2 -> findViewById<TextView>(R.id.dailyStatus2).text = dailyStatus
                        3 -> findViewById<TextView>(R.id.dailyStatus3).text = dailyStatus
                        4 -> findViewById<TextView>(R.id.dailyStatus4).text = dailyStatus
                        5 -> findViewById<TextView>(R.id.dailyStatus5).text = dailyStatus
                    }

                    when (i) {
                        0 -> findViewById<TextView>(R.id.dailyTemp).text = tempMin + tempMax
                        1 -> findViewById<TextView>(R.id.dailyTemp1).text = tempMin + tempMax
                        2 -> findViewById<TextView>(R.id.dailyTemp2).text = tempMin + tempMax
                        3 -> findViewById<TextView>(R.id.dailyTemp3).text = tempMin + tempMax
                        4 -> findViewById<TextView>(R.id.dailyTemp4).text = tempMin + tempMax
                        5 -> findViewById<TextView>(R.id.dailyTemp5).text = tempMin + tempMax
                    }
                }

                //for (i in 0 until jsonArrayHourly.length()) {
                    //val hourlyTemp = jsonArrayHourly.getJSONObject(0).getJSONObject("temp")
                    //val dt = jsonArrayHourly.getJSONObject(0).getLong("dt")
                    //findViewById<TextView>(R.id.testHourly).text = dt
                    //indViewById<TextView>(R.id.testHourly).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(dt*1000))

                //}


                val updatedAt:Long = current.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = current.getString("temp")+"°C"
                val pressure = current.getString("pressure") + " hPa"
                val humidity = current.getString("humidity") + "%"
                val windSpeed = current.getString("wind_speed") + " km/h"
                val windDeg = current.getString("wind_deg")


                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        }

    }
}