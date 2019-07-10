package hr.tvz.android.listazdelarec

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class FrescoInitializer : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}