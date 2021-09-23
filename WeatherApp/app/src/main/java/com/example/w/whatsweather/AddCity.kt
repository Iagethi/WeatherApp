package com.example.w.whatsweather

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.Toast
import com.example.w.whatsweather.data.City
import com.example.w.whatsweather.data.WeatherResult
import kotlinx.android.synthetic.main.dialog_add_city.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCity : DialogFragment() {

    interface CityHandler {
        fun cityAdded(city: City)
        fun cityUpdated(city: City)
    }

    private lateinit var cityHandler: CityHandler

    private lateinit var etCity: EditText

    var currentIcon: String = ""
    var currentTemp: String = ""

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is CityHandler) {
            cityHandler = context
        }
        else {
            throw RuntimeException("The Activity does not implement the CityHandler interface")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_city))

        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_city, null)

        etCity = rootView.etCity

        builder.setView(rootView)

        builder.setPositiveButton(getString(R.string.ok)) {
            dialog, witch ->
        }
        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (etCity.text.isNotEmpty()) {
                handleCityAddedWithBasicWeatherInfo(etCity.text.toString())
                handleCityAdded()
                dialog.dismiss()
            } else {
                etCity.error = getString(R.string.empty_field)
            }
        }
    }

   private fun handleCityAdded() {
        cityHandler.cityAdded(
                City(null, etCity.text.toString(), currentTemp, currentIcon)
        )
    }

    fun handleCityAddedWithBasicWeatherInfo(city: String) {
        val weatherCall = (context as MainActivity).weatherAPI.getWeather(city)

        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherResult = response.body()
                currentIcon = weatherResult?.weather?.get(0)?.icon.toString()
                currentTemp = weatherResult?.main?.temp.toString()

                handleCityAdded()

            }
        })
    }

}