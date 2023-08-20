package el.sft.bw

import android.app.Application

class App : Application() {
    companion object {
        lateinit var current: App
    }

    override fun onCreate() {
        super.onCreate()
        current = this;
    }
}