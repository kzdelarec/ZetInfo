package hr.tvz.android.listazdelarec.entities

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import hr.tvz.android.listazdelarec.R
import org.jsoup.Jsoup
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class DataScraper (num : String, dir : Int, cb : AsyncTaskFinishListener<ArrayList<TramDetails>>, context : Context) : AsyncTask<Void, Void, String>() {

    private val DB = "jdbc:mysql://remotemysql.com:3306/am6e7yMEu0"
    private val USER = "am6e7yMEu0"
    private val PASS = "eiYdHe8QfR"

    var tramDetailsList : ArrayList<TramDetails> = ArrayList()
    var lineNumber = num
    var lineDirection = dir
    var callback : AsyncTaskFinishListener<ArrayList<TramDetails>> = cb
    var focusId = 0

    var dialog = Dialog(context)


    override fun onPreExecute() {
        super.onPreExecute()
        dialog.setContentView(R.layout.progress_dialog)
        dialog.show()
    }

    override fun doInBackground(vararg params: Void?): String? {

        var conn : Connection = DriverManager.getConnection(DB, USER, PASS)

        Log.i("baza", "spojio sam se na bazu")

        try {
            var stmt = conn.prepareStatement("SELECT * FROM lineTimesTest WHERE line = ? and direction = ?")
            stmt.setInt(1, lineNumber.toInt())
            stmt.setInt(2, lineDirection)

            var resultSet = stmt.executeQuery()
            Log.i("baza", "izvršio sam upit")
            var i = 0
            while (resultSet.next()){
                i++
                var departure = resultSet.getString("departure")
                var arrival = resultSet.getString("arrival")
                checkForfocus(departure, arrival, i)
                var tramDetails = TramDetails(departure, arrival)
                tramDetailsList.add(tramDetails)
            }
        } catch(e : Exception){
            e.printStackTrace()
        } finally {
            conn.close()
        }

        return null
    }

    private fun checkForfocus(departure: String?, arrival: String?, i : Int) {

        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val arr = LocalTime.parse(arrival, formatter)
        val dep = LocalTime.parse(departure, formatter)
        val now = LocalTime.now()

        if (now.isBefore(arr) && now.isAfter(dep)) {
            focusId = i
        }
    }

    override fun onPostExecute(result: String?) {
        if(dialog != null){
            dialog.dismiss()
        }
        if(!tramDetailsList.isEmpty() || tramDetailsList != null){
            if(callback != null){
                callback.onTaskComplete(tramDetailsList, focusId)
            }
        }
    }
}