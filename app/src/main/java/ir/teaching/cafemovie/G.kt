package ir.teaching.cafemovie

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate




class G : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_AUTO_TIME
        )
    }
}