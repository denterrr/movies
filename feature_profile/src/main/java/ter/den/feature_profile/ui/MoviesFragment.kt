package ter.den.feature_profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import ter.den.core.domain.extensions.repeatOnCreatedLifecycle
import ter.den.core.domain.model.CustomThrowable
import ter.den.core.presentation.ViewModelFactory
import ter.den.feature_movie_details.ui.DetailsFragment
import ter.den.feature_profile.R
import ter.den.feature_profile.databinding.FragmentMoviesBinding
import ter.den.feature_profile.di.ProfileComponentViewModel
import ter.den.feature_profile.presentation.MovieAdapter
import ter.den.feature_profile.presentation.MovieStateAdapter
import ter.den.feature_profile.presentation.ProfileViewModel
import javax.inject.Inject

class MoviesFragment : Fragment() {

    private var movieType: String? = null

    private var _binding: FragmentMoviesBinding? = null
    private val binding
        get() = _binding ?: throw CustomThrowable.BindingNull

    private val movieAdapter by lazy {
        val onClickListener: ((Int, String) -> Unit) = { id, title ->
            findNavController().navigate(
                R.id.action_moviesFragment_to_detailsFragment, bundleOf(
                    DetailsFragment.MOVIE_ID_KEY to id,
                    DetailsFragment.MOVIE_TITLE_KEY to title
                )
            )
        }
        MovieAdapter(onClickListener = onClickListener) { id ->
            viewModel.onFavoriteClick(id)
        }
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ProfileViewModel
            by viewModels(factoryProducer = { viewModelFactory })


    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<ProfileComponentViewModel>()
            .profileComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentMoviesBinding.inflate(layoutInflater)
        parseParams()
        return binding.root
    }

    private fun parseParams() {
        movieType = arguments?.getString(MOVIE_TYPE)
        binding.toolBarMovies.title = movieType
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        initRvAdapter()
        initObservers()
    }

    private fun initClickListeners() {
        binding.toolBarMovies.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.accountDetails.observe(viewLifecycleOwner) {
            when (movieType) {
                FAVORITES -> initFavoritesObserver()
                WATCH_LIST -> initWatchListObserver()
            }
        }
    }

    private fun initRvAdapter() {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvMovies.adapter = movieAdapter.withLoadStateHeaderAndFooter(
            MovieStateAdapter(movieAdapter),
            MovieStateAdapter(movieAdapter)
        )
        binding.rvMovies.layoutManager = layoutManager
        movieAdapter.addLoadStateListener { state ->
            binding.rvMovies.isVisible = state.refresh != LoadState.Loading
            binding.progress.isVisible = state.refresh == LoadState.Loading
            binding.tvEmpty.isVisible =
                (movieAdapter.itemCount == 0 && state.refresh is LoadState.NotLoading)
            if (state.refresh is LoadState.Error) {
                Snackbar.make(
                    binding.root,
                    (state.refresh as LoadState.Error).error.localizedMessage ?: "Error",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Retry") {
                        movieAdapter.retry()
                    }.show()
            }

        }
    }

    private fun initFavoritesObserver() {
        repeatOnCreatedLifecycle {
            viewModel.favoriteContent.collectLatest(movieAdapter::submitData)
        }
    }

    private fun initWatchListObserver() {
        repeatOnCreatedLifecycle {
            viewModel.watchListContent.collectLatest(movieAdapter::submitData)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val MOVIE_TYPE = "MOVIE_TYPE"
        const val FAVORITES = "Favorites"
        const val WATCH_LIST = "Watch list"
    }
}