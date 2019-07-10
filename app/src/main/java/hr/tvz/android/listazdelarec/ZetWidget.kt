package hr.tvz.android.listazdelarec

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.widget.Toast
import hr.tvz.android.listazdelarec.entities.ServiceBuilder
import hr.tvz.android.listazdelarec.entities.ServiceInterface
import hr.tvz.android.listazdelarec.entities.TramDetails
import hr.tvz.android.listazdelarec.entities.TramLine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter


/**
 * Implementation of App Widget functionality.
 */
class ZetWidget : AppWidgetProvider() {

    private var focusId: Int = 0

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        initialize(appWidgetIds, context, appWidgetManager)
    }

    private fun initialize(
        appWidgetIds: IntArray?,
        context: Context?,
        appWidgetManager: AppWidgetManager?
    ) {
        val tramService = ServiceBuilder.buildService(ServiceInterface::class.java)
        val requestCall = tramService.getTramList("day")
        lateinit var tram: TramLine

        requestCall.enqueue(object : Callback<List<TramLine>> {
            override fun onResponse(call: Call<List<TramLine>>, response: Response<List<TramLine>>) {
                if (response.isSuccessful) {
                    val tramList = response.body()
                    tram = tramList!!.last()
                    updateUITram(appWidgetIds, context, appWidgetManager, tram)
                    getDetails(tram.number!!.toInt(), 0, context!!, appWidgetIds, appWidgetManager)
                }
            }

            override fun onFailure(call: Call<List<TramLine>>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun updateUITram(appWidgetIds: IntArray?, context: Context?, appWidgetManager: AppWidgetManager?, tram: TramLine) {
        for (i in 0 until appWidgetIds!!.size) {
            val remoteViews = RemoteViews(context!!.packageName, R.layout.zet_widget)
            appWidgetManager!!.updateAppWidget(i, remoteViews)
            if (tram != null) {
                remoteViews.setTextViewText(R.id.lineNumberW, tram.number)
                remoteViews.setTextViewText(R.id.lineRouteW, tram.route)
                appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews)
            }

        }
    }

    private fun updateUIDetails(appWidgetIds: IntArray?, context: Context?, appWidgetManager: AppWidgetManager?, details: TramDetails) {
        for (i in 0 until appWidgetIds!!.size) {
            val remoteViews = RemoteViews(context!!.packageName, R.layout.zet_widget)
            appWidgetManager!!.updateAppWidget(i, remoteViews)
            if (details != null) {
                remoteViews.setTextViewText(R.id.arrivalW, details.arrival)
                remoteViews.setTextViewText(R.id.departureW, details.departure)
                appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews)
            }

        }
    }

    private fun getDetails(tramLine : Int, direction : Int, context : Context, appWidgetIds: IntArray?, appWidgetManager: AppWidgetManager?){
        val tramService = ServiceBuilder.buildService(ServiceInterface::class.java)
        val requestCall = tramService.getDetailsList(tramLine, direction)

        requestCall.enqueue(object : Callback<List<TramDetails>> {

            override fun onResponse(call: Call<List<TramDetails>>, response: Response<List<TramDetails>>) {
                if (response.isSuccessful) {
                    val tramList = response.body()
                    for(i in 0 until  tramList!!.size step 1){
                        checkForFocus(tramList[i].departure,tramList[i].arrival, i )
                    }
                    val tramDetails = tramList[focusId]
                    updateUIDetails(appWidgetIds, context,appWidgetManager,tramDetails)
                }
            }

            override fun onFailure(call: Call<List<TramDetails>>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkForFocus(departure: String?, arrival: String?, i : Int) {

        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val arr = LocalTime.parse(arrival, formatter)
        val dep = LocalTime.parse(departure, formatter)
        val now = LocalTime.now()

        if (now.isBefore(arr) && now.isAfter(dep)) {
            focusId = i
        }
    }
}

