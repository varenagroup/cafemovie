package ir.teaching.cafemovie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.teaching.cafemovie.R
import ir.teaching.cafemovie.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}