package ter.den.movie.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import ter.den.movie.R
import ter.den.movie.appComponent
import ter.den.movie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException()

//    @Inject
//    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.navFragmentContainer) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHost.navController)
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.isVisible =
                destination.id !in listOf(
                    ter.den.feature_home.R.id.detailsFragment,
                    ter.den.feature_profile.R.id.moviesFragment
                )
            binding.divider.isVisible = binding.bottomNavigation.isVisible
        }

        binding.bottomNavigation.setOnItemReselectedListener { menuItem ->
            val recyclerView =
                when (menuItem.itemId) {
                    R.id.home_tab_navigation -> navHost.requireView()
                        .findViewById<RecyclerView>(
                            ter.den.feature_home.R.id.rvPopular
                        )
                    R.id.search_tab_navigation -> navHost.requireView()
                        .findViewById<RecyclerView>(
                            ter.den.feature_search.R.id.rvMovies
                        )
                    else -> null
                }
            val appBar =
                when (menuItem.itemId) {
                    R.id.home_tab_navigation -> navHost.requireView()
                        .findViewById<AppBarLayout>(
                            ter.den.feature_home.R.id.appBarHome
                        )
                    R.id.search_tab_navigation -> navHost.requireView()
                        .findViewById<AppBarLayout>(
                            ter.den.feature_search.R.id.appBarSearch
                        )
                    else -> null
                }
            recyclerView?.scrollToPosition(0)
            appBar?.setExpanded(true, true)
        }

    }


}