package com.example.w.whatsweather

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import com.example.w.whatsweather.adapter.CitiesListAdapter
import com.example.w.whatsweather.data.AppDatabase
import com.example.w.whatsweather.data.City
import com.example.w.whatsweather.network.WeatherAPI
import com.example.w.whatsweather.network.WeatherInfoAPI
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), AddCity.CityHandler {

    lateinit var citiesListAdapter: CitiesListAdapter

    private var editIndex: Int = 0

    lateinit var weatherInfoAPI: WeatherInfoAPI
    lateinit var weatherAPI: WeatherAPI
    private val HOST_URL = "https://api.openweathermap.org/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            showAddCityDialog()
        }

        initRetrofit()
        initRecyclerView()
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        weatherAPI = retrofit.create(WeatherAPI::class.java)
        weatherInfoAPI = retrofit.create(WeatherInfoAPI::class.java)
    }

    private fun initRecyclerView() {
        Thread {
            val cities = AppDatabase.getInstance(this@MainActivity).citiesListDao().findAllCities()

            citiesListAdapter = CitiesListAdapter(this@MainActivity, cities)
            runOnUiThread {
                recyclerCities.adapter = citiesListAdapter
            }
        }.start()
    }

    private fun showAddCityDialog() {
        AddCity().show(supportFragmentManager, "TAG_CREATE")
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun cityAdded(city: City) {
        Thread {
            val id = AppDatabase.getInstance(this).citiesListDao().insertCity(city)
            city.cityId = id

            runOnUiThread {
                citiesListAdapter.addCity(city)
            }
        }.start()
    }

    override fun cityUpdated(city: City) {
        val dbThread = Thread {
            AppDatabase.getInstance(this@MainActivity).citiesListDao().updateCity(city)
            runOnUiThread { citiesListAdapter.updateCity(city, editIndex) }
        }
        dbThread.start()
    }
}
