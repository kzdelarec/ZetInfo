package hr.tvz.android.listazdelarec.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import hr.tvz.android.listazdelarec.R
import hr.tvz.android.listazdelarec.entities.TramDetails
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class DetailsAdapter (detailsList : List<TramDetails>, context : Context) : RecyclerView.Adapter<DetailsAdapter.DetailsHolder>() {

    var detailsList = detailsList
    var appContext = context

    class DetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var departure : TextView = itemView.findViewById(R.id.departure)
        var arrival : TextView = itemView.findViewById(R.id.arrival)
        var icon : ImageView = itemView.findViewById(R.id.tripIcon)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DetailsAdapter.DetailsHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(p0.context)
        val view : View = layoutInflater.inflate(R.layout.details_card, p0, false)
        val holder  = DetailsAdapter.DetailsHolder(view)
        return  holder
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    override fun onBindViewHolder(p0: DetailsHolder, p1: Int) {
        var tramDetail : TramDetails = detailsList.get(p1)
        p0.departure.text = tramDetail.departure
        p0.arrival.text = tramDetail.arrival

        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val arr = LocalTime.parse(p0.arrival.text, formatter)
        val dep = LocalTime.parse(p0.departure.text, formatter)
        val midnight = LocalTime.parse("00:00:00", formatter)
        val now = LocalTime.now()

        if (dep.isBefore(now) && arr.isBefore(now)) {
            p0.icon.setImageResource(R.drawable.line)
        } else if (now.isBefore(arr) && now.isAfter(dep)) {
            p0.icon.setImageResource(R.drawable.arrows)
        } else if(now.isBefore(dep) and now.isAfter(midnight)){
            p0.icon.setImageResource(R.drawable.more)
        } else {
            p0.icon.setImageResource(R.drawable.more)
        }
    }


}