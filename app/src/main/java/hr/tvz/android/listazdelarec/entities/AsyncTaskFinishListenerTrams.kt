package hr.tvz.android.listazdelarec.entities

interface AsyncTaskFinishListenerTrams<T> {

    fun onTaskComplete(result: ArrayList<TramLine>)

}