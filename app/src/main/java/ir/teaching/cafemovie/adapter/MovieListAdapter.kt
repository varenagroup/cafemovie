package ir.teaching.cafemovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import coil.load
import ir.teaching.cafemovie.R
import ir.teaching.cafemovie.data.Result
import ir.teaching.cafemovie.databinding.MoviesItemLayoutBinding

internal class MovieListAdapter(
    private var movieList: List<Result>
) : BaseAdapter() {

    fun setUpdatedList(movieList: List<Result>) {
        this.movieList = movieList
    }

    override fun getCount(): Int {
        return movieList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val holder: ViewHolder
        if (convertView == null) {
            val itemBinding: MoviesItemLayoutBinding =
                MoviesItemLayoutBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
            holder = ViewHolder(itemBinding)
            holder.view = itemBinding.root
            holder.view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val result = movieList[position];

        holder.binding.imgMovie.load("https://media.themoviedb.org/t/p/w220_and_h330_face${result.poster_path}")
        {
            placeholder(R.mipmap.no_poster_placeholder)
        }
        holder.binding.txtTitle.text = result.title

        return holder.view
    }

    private class ViewHolder(binding: MoviesItemLayoutBinding) {
        var view: View
        var binding: MoviesItemLayoutBinding

        init {
            view = binding.root
            this.binding = binding
        }
    }
}