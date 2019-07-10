package hr.tvz.android.listazdelarec.entities

import android.os.Parcel
import android.os.Parcelable

class TramLine(var number: String?, var route: String?, var imageName: String?, var status: String?) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(number)
        writeString(route)
        writeValue(imageName)
        writeValue(status)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TramLine> = object : Parcelable.Creator<TramLine> {
            override fun createFromParcel(source: Parcel): TramLine = TramLine(source)
            override fun newArray(size: Int): Array<TramLine?> = arrayOfNulls(size)
        }
    }
}
