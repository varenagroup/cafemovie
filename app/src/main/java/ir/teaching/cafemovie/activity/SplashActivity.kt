package ir.teaching.cafemovie.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import ir.teaching.cafemovie.R
import ir.teaching.cafemovie.databinding.ActivitySplashBinding
import ir.teaching.cafemovie.databinding.FragmentMainBinding
import ir.teaching.cafemovie.movieList
import ir.teaching.cafemovie.ui.main.MainViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        requestServer()
    }

    private fun requestServer() {
        viewModel.getUpcomingList(1)
        viewModel.upcomingList.observe(this) {
            it?.also {
                movieList = it
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } ?: connectionFailed()
        }
    }

    private fun connectionFailed() {
        binding.layConnectionFailed.visibility = View.VISIBLE
        binding.layLoading.visibility = View.GONE
        binding.txtRetry.setOnClickListener {
            binding.layConnectionFailed.visibility = View.GONE
            binding.layLoading.visibility = View.VISIBLE
            requestServer()
        }
    }
}