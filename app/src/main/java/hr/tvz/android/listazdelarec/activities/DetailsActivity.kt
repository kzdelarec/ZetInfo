package hr.tvz.android.listazdelarec.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import hr.tvz.android.listazdelarec.R
import hr.tvz.android.listazdelarec.adapters.DetailsAdapter
import hr.tvz.android.listazdelarec.adapters.ListCardAdapter
import hr.tvz.android.listazdelarec.entities.*
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.details_card.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class DetailsActivity : Fragment() {

    var list : ArrayList<TramDetails> = ArrayList()
    lateinit var tramLine : TramLine
    var focusId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var bundle = arguments
        tramLine= arguments?.get("obj") as TramLine

        from.text = arguments!!.getString("from")
        to.text = arguments!!.getString("to")

        setEventListeners(bundle!!, tramLine)

        val uri = Uri.parse(arguments!!.getString("imgUrl"))
        routeImg.setImageURI(uri)

        getData(0)

    }

    private fun getData(direction : Int) {
        val tramService = ServiceBuilder.buildService(ServiceInterface::class.java)
        val requestCall = tramService.getDetailsList(tramLine.number!!.toInt(), direction)

        var dialog = Dialog(activity as Context)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.show()
        var mp = MediaPlayer.create(activity as Context, R.raw.notif)
        //mp.prepare()

        requestCall.enqueue(object : Callback<List<TramDetails>> {


            override fun onResponse(call: Call<List<TramDetails>>, response: Response<List<TramDetails>>) {
                if (response.isSuccessful) {
                    val tramList = response.body()

                    for(i in 0 until  tramList!!.size step 1){
                        checkForfocus(tramList[i].departure,tramList[i].arrival, i )
                    }

                    detailsList.setHasFixedSize(true)
                    detailsList.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
                    detailsList.adapter = DetailsAdapter(tramList!!, activity?.applicationContext!!)
                    detailsList.smoothScrollToPosition(focusId)
                    dialog.dismiss()
                    mp.start()


                }
            }

            override fun onFailure(call: Call<List<TramDetails>>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(activity as Context, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.pdfAction -> {
                var intent : Intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.zet.hr/UserDocsImages/voznired/" + tramLine.number.toString() + ".pdf"))
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun setEventListeners(extraData : Bundle, tramLine : TramLine){
        var direction = 0
        switchBtn.setOnClickListener(){
            var anim  = AnimationUtils.loadAnimation(activity?.applicationContext!!, R.anim.rotation)
            switchBtn.startAnimation(anim)
            var lineNumber = tramLine.number.toString()
            if(direction == 0) {
                to.text = extraData.getString("from")
                from.text = extraData.getString("to")
                direction = 1
            } else {
                from.text = extraData.getString("from")
                to.text = extraData.getString("to")
                direction = 0
            }
            getData(direction)

        }

        routeImg.setOnClickListener() {
            //var id = tramLine.image!!

            var fragment : Fragment = ImageActivity()
            var args : Bundle = Bundle()
            args.putString("id", arguments!!.getString("imgUrl"))
            fragment.arguments = args

            val activity = activity as DrawerActivity
            val fragmentManager = activity.supportFragmentManager
            val supportFragmentManager = fragmentManager.beginTransaction()
            supportFragmentManager.replace(R.id.replace,fragment).addToBackStack("").commit()
        }
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




}
