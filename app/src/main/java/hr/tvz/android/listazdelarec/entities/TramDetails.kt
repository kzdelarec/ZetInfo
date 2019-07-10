package hr.tvz.android.listazdelarec.entities

import android.os.Parcel
import android.os.Parcelable

class TramDetails(var departure: String?, var arrival: String?) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(departure)
        writeString(arrival)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TramDetails> = object : Parcelable.Creator<TramDetails> {
            override fun createFromParcel(source: Parcel): TramDetails = TramDetails(source)
            override fun newArray(size: Int): Array<TramDetails?> = arrayOfNulls(size)
        }
    }
}