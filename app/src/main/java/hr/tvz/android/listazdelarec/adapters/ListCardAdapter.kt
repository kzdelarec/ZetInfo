package hr.tvz.android.listazdelarec.adapters


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hr.tvz.android.listazdelarec.R
import hr.tvz.android.listazdelarec.entities.TramLine
import hr.tvz.android.listazdelarec.activities.DetailsActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManagerNonConfig
import android.support.v7.app.AppCompatActivity
import hr.tvz.android.listazdelarec.activities.DrawerActivity

class ListCardAdapter(lineList : List<TramLine>, context : Context): RecyclerView.Adapter<ListCardAdapter.ListCardHolder>() {

    var tramList = lineList
    var appcontext = context

    class ListCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var num : TextView = itemView.findViewById(R.id.lineNumber)
        var route : TextView = itemView.findViewById(R.id.lineRoute)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListCardHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(p0.context)
        val view : View = layoutInflater.inflate(R.layout.list_card, p0, false)
        var holder  = ListCardHolder(view)
        return  holder
    }

    override fun getItemCount(): Int {
        return tramList.size
    }

    override fun onBindViewHolder(p0: ListCardHolder, p1: Int) {
        var currentTram: TramLine = tramList[p1]
        p0.num.text = currentTram.number
        p0.route.text = currentTram.route
        var imageLink = currentTram.imageName
        p0.itemView.setOnClickListener {
            var fragment : Fragment = DetailsActivity()
            var args : Bundle = Bundle()
            var index = p0.route.text.indexOf("-")
            args.putString("from", p0.route.text.substring(0,index-1))
            args.putString("to", p0.route.text.substring(index+2, p0.route.text.length ))
            args.putParcelable("obj", currentTram)
            args.putString("imgUrl", imageLink)

            fragment.arguments = args

            val activity = appcontext as DrawerActivity
            val fragmentManager = activity.supportFragmentManager
            val supportFragmentManager = fragmentManager.beginTransaction()
            supportFragmentManager.replace(R.id.replace,fragment).addToBackStack("").commit()

            //startActivity(appcontext,intent,null)
        }
    }

}