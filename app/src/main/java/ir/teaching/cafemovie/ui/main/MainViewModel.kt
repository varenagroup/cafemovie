package ir.teaching.cafemovie.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.teaching.cafemovie.data.Upcoming
import ir.teaching.cafemovie.server.MovieApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private var _upcomingList = MutableLiveData<Response<Upcoming>?>()
    var upcomingList: MutableLiveData<Response<Upcoming>?> = _upcomingList

    fun getUpcomingList(page: Int) {
        MovieApi.RetrofitInstance.api.upcoming(page)
            .enqueue(object :
                Callback<Upcoming> {
                override fun onResponse(call: Call<Upcoming>, response: Response<Upcoming>) {
                    Log.i("LOG", "upcoming list response: $response")
                    if (response.body() != null) {
                        _upcomingList.value = response
                    } else {
                        _upcomingList.value = null
                        Log.i("LOG", "upcoming list response body is null")
                    }
                }

                override fun onFailure(call: Call<Upcoming>, t: Throwable) {
                    _upcomingList.value = null
                    Log.i("LOG", "upcoming list failure: " + t.message.toString())
                }
            })
    }
}