package hr.tvz.android.listazdelarec.entities

interface AsyncTaskFinishListener<T> {

    fun onTaskComplete(result: ArrayList<TramDetails>, focusId : Int)

}