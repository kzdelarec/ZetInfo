package hr.tvz.android.listazdelarec.recievers_and_broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatDelegate
import android.widget.Toast
import hr.tvz.android.listazdelarec.R
import hr.tvz.android.listazdelarec.activities.MainActivity

class LowBatteryBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Slaba mi je baterija registrirani", Toast.LENGTH_LONG).show()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        var intent = Intent(context,MainActivity::class.java)
        startActivity(context, intent, null)
    }

}