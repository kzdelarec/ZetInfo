package hr.tvz.android.listazdelarec.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import hr.tvz.android.listazdelarec.adapters.ListCardAdapter
import hr.tvz.android.listazdelarec.R
import kotlinx.android.synthetic.main.activity_main.*
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Toast
import hr.tvz.android.listazdelarec.entities.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_main, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val tramService = ServiceBuilder.buildService(ServiceInterface::class.java)
        val requestCall = tramService.getTramList(arguments!!.getString("status"))

        var dialog = Dialog(activity as Context)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.show()

        requestCall.enqueue(object : Callback<List<TramLine>>{
            override fun onResponse(call: Call<List<TramLine>>, response: Response<List<TramLine>>) {
                if(response.isSuccessful){
                    val tramList = response.body()

                    recList.setHasFixedSize(true)
                    recList.layoutManager = LinearLayoutManager(activity as Context)
                    recList.adapter = ListCardAdapter(tramList!!, activity as Context)
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<List<TramLine>>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(activity as Context, "Error", Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.internetAction -> {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.zet.hr/tramvajski-prijevoz/dnevne-linije/249"))
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
