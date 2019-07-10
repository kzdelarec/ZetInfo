package hr.tvz.android.listazdelarec.entities

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import hr.tvz.android.listazdelarec.R
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class TramExtractor (cb : AsyncTaskFinishListenerTrams<ArrayList<TramLine>>, context : Context, searchStatus : String) : AsyncTask<Void, Void, String>() {

    private val DB = "jdbc:mysql://remotemysql.com:3306/am6e7yMEu0"
    private val USER = "am6e7yMEu0"
    private val PASS = "eiYdHe8QfR"

    var trams : ArrayList<TramLine> = ArrayList()
    var searchStatus = searchStatus
    var context = context
    var callback : AsyncTaskFinishListenerTrams<ArrayList<TramLine>> = cb

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
            var stmt = conn.prepareStatement("SELECT * FROM trams WHERE status = ? ")
            stmt.setString(1, searchStatus)

            var resultSet = stmt.executeQuery()
            Log.i("baza", "izvršio sam upit")
            var i = 0
            while (resultSet.next()){
                i++
                var imageName = resultSet.getString("imageName")
                var imageId = context.resources.getIdentifier(imageName,"drawable",context.packageName)
                var tram = TramLine(resultSet.getString("number"), resultSet.getString("route"), "bla", resultSet.getString("status"))
                trams.add(tram)
            }
        } catch(e : Exception){
            e.printStackTrace()
        } finally {
            conn.close()
        }

        return null
    }


    override fun onPostExecute(result: String?) {
        if(dialog != null){
            dialog.dismiss()
        }
        if(!trams.isEmpty() || trams != null){
            if(callback != null){
                callback.onTaskComplete(trams)
            }
        }
    }
}