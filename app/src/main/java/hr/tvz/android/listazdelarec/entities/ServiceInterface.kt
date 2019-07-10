package hr.tvz.android.listazdelarec.entities

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceInterface {
    @GET("lines.php")
    fun getTramList(@Query("status") status : String): Call<List<TramLine>>

    @GET("lines.php")
    fun getDetailsList(@Query("line") line : Int, @Query("direction") direction : Int): Call<List<TramDetails>>
}