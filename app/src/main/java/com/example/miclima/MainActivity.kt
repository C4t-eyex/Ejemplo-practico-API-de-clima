package com.example.miclima

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var etCity: EditText
    private lateinit var tvWeatherInfo: TextView
    private lateinit var btnSearch: Button

    private val apiKey = "7301a92d2d27eddf29dd48282a6f75ad"  // la API Key de OpenWeatherMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a los elementos de la interfaz
        etCity = findViewById(R.id.etCity)
        tvWeatherInfo = findViewById(R.id.tvWeatherInfo)
        btnSearch = findViewById(R.id.btnSearch)

        // Acción del botón para obtener el clima
        btnSearch.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                getWeather(city)
            }
        }
    }

    private fun getWeather(city: String) {
        RetrofitInstance.api.getCurrentWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val temperature = weatherResponse?.main?.temp
                    runOnUiThread {
                        tvWeatherInfo.text = "La temperatura en $city es $temperature °C"
                    }
                } else {
                    Log.e("Clima", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("Clima", "Error en la solicitud: ${t.message}")
                runOnUiThread {
                    tvWeatherInfo.text = "No se pudo obtener el clima"
                }
            }
        })
    }
}

