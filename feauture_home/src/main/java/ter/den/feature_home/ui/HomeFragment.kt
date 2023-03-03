package ter.den.feature_home.ui

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
import ter.den.feature_home.R
import ter.den.feature_home.databinding.FragmentHomeBinding
import ter.den.feature_home.di.HomeComponentViewModel
import ter.den.feature_home.presentation.HomeViewModel
import ter.den.feature_home.presentation.MovieAdapter
import ter.den.feature_home.presentation.PopularStateAdapter
import ter.den.feature_movie_details.ui.DetailsFragment
import javax.inject.Inject


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding ?: throw CustomThrowable.BindingNull

    private val movieAdapter by lazy {
        val onClickListener: ((Int, String) -> Unit) = { id, title ->
            findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment, bundleOf(
                    DetailsFragment.MOVIE_ID_KEY to id,
                    DetailsFragment.MOVIE_TITLE_KEY to title
                )
            )
        }
        MovieAdapter(onClickListener = onClickListener) { id ->
            viewModel.onClickFavorite(id)
        }
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: HomeViewModel
            by viewModels(factoryProducer = { viewModelFactory })


    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<HomeComponentViewModel>()
            .homeComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvAdapter()
        initObservers()
    }

    private fun initRvAdapter() {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvPopular.adapter = movieAdapter.withLoadStateHeaderAndFooter(
            PopularStateAdapter(movieAdapter),
            PopularStateAdapter(movieAdapter)
        )
        binding.rvPopular.layoutManager = layoutManager
        movieAdapter.addLoadStateListener { state ->
            binding.rvPopular.isVisible = state.refresh != LoadState.Loading
            binding.progress.isVisible = state.refresh == LoadState.Loading
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

    private fun initObservers() {
        repeatOnCreatedLifecycle {
            viewModel.getPopularContent().collectLatest(movieAdapter::submitData)
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}