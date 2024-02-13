package ir.teaching.cafemovie.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val actionbar = addCustomActionBarLayout()

        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            actionbar.findViewById<DayNightSwitch>(R.id.swb_nightMode).setIsNight(true)
        }

        viewModel.getUpcomingList(1);
        viewModel.upcomingList.observe(viewLifecycleOwner) {
            it?.also { response ->
                val body = response.body()!!
                Log.i("LOG", "upcoming body: $body")
                val movieListAdapter = MovieListAdapter(body.results)
                binding.grdMovie.apply {
                    adapter = movieListAdapter
                }
            } ?: Log.i("LOG", "value is null")
        }

        return root
    }

    fun addCustomActionBarLayout(): Toolbar {
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
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return actionbarMainView
    }

}