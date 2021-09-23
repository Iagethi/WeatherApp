package com.example.w.whatsweather.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.w.whatsweather.DetailsActivity
import com.example.w.whatsweather.MainActivity
import com.example.w.whatsweather.R
import com.example.w.whatsweather.data.AppDatabase
import com.example.w.whatsweather.data.City
import kotlinx.android.synthetic.main.city_row.view.*

class CitiesListAdapter : RecyclerView.Adapter<CitiesListAdapter.ViewHolder> {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCity = itemView.tvCity
        val btnDelete = itemView.btnDelete
    }

    companion object {
        val KEY_DATA = "KEY_DATA"
    }

    lateinit var currentCity: City

    var cities = mutableListOf<City>()

    val context : Context

    constructor(context: Context, cities: List<City>) : super() {
        this.context = context
        this.cities.addAll(cities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.city_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]
        currentCity = city

        holder.tvCity.text = city.name

        holder.btnDelete.setOnClickListener {
            deleteCity(holder.adapterPosition)
        }

        holder.itemView.setOnClickListener {

            var intentStart = Intent()
            intentStart.setClass(context, DetailsActivity::class.java)
            intentStart.putExtra(KEY_DATA, city.name)

            context.startActivity(intentStart)
        }
    }

    fun addCity(city: City) {
        cities.add(city)
        notifyItemInserted(cities.lastIndex)
    }

    fun updateCity(city: City, idx: Int) {
        cities[idx] = city
        notifyItemChanged(idx)
    }

    private fun deleteCity(adapterPosition: Int) {

        Thread {
            AppDatabase.getInstance(context).citiesListDao().deleteCity(cities[adapterPosition])

            cities.removeAt(adapterPosition)
            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

}