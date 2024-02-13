package ir.teaching.cafemovie.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.teaching.cafemovie.data.Upcoming
import ir.teaching.cafemovie.server.MovieApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.teaching.cafemovie.data.Result

class MainViewModel : ViewModel() {

    private var _upcomingList = MutableLiveData<List<Result>>()
    var upcomingList: MutableLiveData<List<Result>> = _upcomingList

    private var movieResultList = ArrayList<Result>()
    private var _totalPage = MutableLiveData<Int>()
    var totalPage: MutableLiveData<Int> = _totalPage

    private var _succeedRequest = MutableLiveData<Boolean>()
    var succeedRequest: MutableLiveData<Boolean> = _succeedRequest

    fun getUpcomingList(page: Int) {
        MovieApi.RetrofitInstance.api.upcoming(page)
            .enqueue(object :
                Callback<Upcoming> {
                override fun onResponse(call: Call<Upcoming>, response: Response<Upcoming>) {
                    Log.i("LOG", "upcoming list response: $response")
                    if (response.body() != null) {
//                        _upcomingList.value = response
                        val data = response.body()!!.results as ArrayList<Result>
                        val list: List<Result> = merge(movieResultList, data)
                        movieResultList = list as ArrayList<Result>
                        _upcomingList.value = list
                        _totalPage.value = response.body()!!.total_pages
                        _succeedRequest.value = true
                    } else {
                        _upcomingList.value = null
                        _succeedRequest.value = false
                        Log.i("LOG", "upcoming list response body is null")
                    }
                }

                override fun onFailure(call: Call<Upcoming>, t: Throwable) {
                    _upcomingList.value = null
                    _succeedRequest.value = false
                    Log.i("LOG", "upcoming list failure: " + t.message.toString())
                }
            })
    }

    private fun <T> merge(first: List<T>, second: List<T>): List<T> {
        return first + second
    }
}