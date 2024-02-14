package ir.teaching.cafemovie.ui.main

import android.content.res.Configuration
import ir.teaching.cafemovie.data.Result
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vimalcvs.switchdn.DayNightSwitch
import ir.teaching.cafemovie.R
import ir.teaching.cafemovie.adapter.MovieListAdapter
import ir.teaching.cafemovie.databinding.FragmentMainBinding
import ir.teaching.cafemovie.movieList


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var themeChanged = false
    private var page = 1
    private var scrollDown: Boolean = true

    private lateinit var movieListAdapter: MovieListAdapter

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

        val actionbar = addCustomActionBarLayout()

        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            actionbar.findViewById<DayNightSwitch>(R.id.swb_nightMode).setIsNight(true)
        }

//        var scrollDown: Boolean

        if (!themeChanged) {
//            viewModel.getUpcomingList(page)
            getMovies(movieList)
        }
        viewModel.upcomingList.observe(viewLifecycleOwner) {
            scrollDown = true
            binding.prgLoading.visibility = View.GONE
            binding.layTryAgain.visibility = View.GONE
            Toast.makeText(activity, "$it", Toast.LENGTH_LONG).show()
            getMovies(it)
//            it?.also { response ->
//                if (page > 1) {
//                    movieListAdapter.setUpdatedList(response)
//                } else {
//                    movieListAdapter = MovieListAdapter(response)
//                }
//                binding.grdMovie.apply {
//                    adapter = movieListAdapter
//
//                    setOnScrollListener(object : AbsListView.OnScrollListener {
//                        override fun onScroll(
//                            view: AbsListView?,
//                            firstVisibleItem: Int,
//                            visibleItemCount: Int,
//                            totalItemCount: Int
//                        ) {
//                            if (firstVisibleItem + visibleItemCount >= totalItemCount && scrollDown) {
//                                binding.prgLoading.visibility = View.VISIBLE
//                                binding.layTryAgain.visibility = View.GONE
//                                scrollDown = false
//                                page++
//                                viewModel.getUpcomingList(page)
//                                Toast.makeText(activity, "end of grid view", Toast.LENGTH_LONG).show()
//                                // End has been reached
//                            }
//                        }
//
//                        override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
//                        }
//                    })
//                }
//            } ?: failedToConnect()
        }

        return root
    }

    private fun getMovies(it: List<Result>?) {
        it?.also { response ->
            if (page > 1) {
                movieListAdapter.setUpdatedList(response)
            } else {
                movieListAdapter = MovieListAdapter(response)
            }
            binding.grdMovie.apply {
                adapter = movieListAdapter

                setOnScrollListener(object : AbsListView.OnScrollListener {
                    override fun onScroll(
                        view: AbsListView?,
                        firstVisibleItem: Int,
                        visibleItemCount: Int,
                        totalItemCount: Int
                    ) {
                        if (firstVisibleItem + visibleItemCount >= totalItemCount && scrollDown) {
                            binding.prgLoading.visibility = View.VISIBLE
                            binding.layTryAgain.visibility = View.GONE
                            scrollDown = false
                            page++
                            viewModel.getUpcomingList(page)
                            Toast.makeText(activity, "end of grid view", Toast.LENGTH_LONG).show()
                            // End has been reached
                        }
                    }

                    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    }
                })
            }
        } ?: failedToConnect()
    }

    private fun failedToConnect() {
        Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
        binding.prgLoading.visibility = View.GONE
        binding.layTryAgain.visibility = View.VISIBLE
        binding.txtTryAgain.setOnClickListener {
            viewModel.getUpcomingList(page)
        }
    }

    private fun addCustomActionBarLayout(): Toolbar {
        val cActivity = activity as AppCompatActivity
        val actionBar: ActionBar? = cActivity.supportActionBar
        if (cActivity.supportActionBar?.isShowing == false) {
            cActivity.supportActionBar?.show()
        }
        actionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayUseLogoEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                cActivity.resources,
                android.R.color.transparent,
                null
            )
        )
        val actionbarMainView: Toolbar =
            LayoutInflater.from(actionBar.themedContext).inflate(R.layout.actionbar, null) as Toolbar
        val params: ActionBar.LayoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        actionbarMainView.setContentInsetsAbsolute(0, 0)
        actionBar.setCustomView(actionbarMainView, params)
        val parent: Toolbar = actionbarMainView.parent as Toolbar
        parent.setContentInsetsAbsolute(0, 0)

        actionbarMainView.findViewById<DayNightSwitch>(R.id.swb_nightMode).setListener {
            themeChanged = true
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return actionbarMainView
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.grdMovie.numColumns = 6
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.grdMovie.numColumns = 3
        }
    }
}