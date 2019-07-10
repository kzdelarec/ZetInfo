package hr.tvz.android.listazdelarec.entities

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ServiceBuilder {

    private const val apiUrl = "http://kristijanzdelarec.000webhostapp.com/zet/api/"

    var httpClient = OkHttpClient.Builder()

    private val builder = Retrofit.Builder().baseUrl(apiUrl)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .client(httpClient.build())

    private val retrofit = builder.build()

    fun <T> buildService(serviceType : Class<T>): T{
        return retrofit.create(serviceType)
    }


}