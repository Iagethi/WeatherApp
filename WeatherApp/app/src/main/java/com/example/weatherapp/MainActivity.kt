package com.example.weatherapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.api.ApiProvider
import com.example.weatherapp.api.ApiService
import com.example.weatherapp.model.CurrentWeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private var apiService: ApiService? = null
    private var coroutineJob: Job? = null

    private val appKey: String = "8a5bdda8eafa142c973cd0409a99d2e4"
    private var countryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCallApi = findViewById<Button>(R.id.buttonCallApi)

        val arrayAdapter: ArrayAdapter<*>
        val cities = arrayOf(
            "Paris", "London", "Antananarivo"
        )

        var mListView = findViewById<ListView>(R.id.countryNameList)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities)
        mListView.adapter = arrayAdapter

        apiService = ApiProvider.createService(ApiService::class.java)

        countryName = "Paris"

        buttonCallApi.setOnClickListener {
            searchData()
        }
    }

    override fun onPause() {
        coroutineJob?.cancel()
        super.onPause()
    }

    private fun searchData() {
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {

            val response = apiService?.getResult(countryName, appKey)

            val textViewCallApi = findViewById<TextView>(R.id.textViewCallApi)

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    //val currentWeather = Gson().fromJson(response.body().toString(), CurrentWeatherResponse::class.java)
                    textViewCallApi.text = response.body().toString()
                }
            }
        }
    }
}