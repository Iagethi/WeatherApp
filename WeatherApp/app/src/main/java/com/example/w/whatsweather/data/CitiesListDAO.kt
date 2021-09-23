package com.example.w.whatsweather.data

import android.arch.persistence.room.*

@Dao
interface CitiesListDAO {

    @Query("SELECT * FROM city")
    fun findAllCities(): List<City>

    @Insert
    fun insertCity(city: City) : Long

    @Delete
    fun deleteCity(city: City)

    @Update
    fun updateCity(city: City)

}