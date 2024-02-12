package ir.teaching.cafemovie.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.teaching.cafemovie.R
import ir.teaching.cafemovie.adapter.MovieListAdapter
import ir.teaching.cafemovie.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getUpcomingList(1);
        viewModel.upcomingList.observe(viewLifecycleOwner) {
            it?.also { response ->
                val body = response.body()!!
                Log.i("LOG", "upcoming body: $body")
                val movieListAdapter = MovieListAdapter(body.results)
                binding.grdMovie.apply {
//                    setExpand(true)
                    adapter = movieListAdapter
                }
            } ?: Log.i("LOG", "value is null")
        }

        return root
    }

}