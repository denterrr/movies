package ter.den.feature_search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import ter.den.core.domain.extensions.hideSoftKeyboard
import ter.den.core.domain.extensions.repeatOnCreatedLifecycle
import ter.den.core.domain.extensions.showSoftKeyboard
import ter.den.core.domain.model.CustomThrowable
import ter.den.core.presentation.ViewModelFactory
import ter.den.feature_movie_details.ui.DetailsFragment
import ter.den.feature_search.R
import ter.den.feature_search.databinding.FragmentSearchBinding
import ter.den.feature_search.di.SearchComponentViewModel
import ter.den.feature_search.domain.extensions.textChanges
import ter.den.feature_search.presentation.MovieAdapter
import ter.den.feature_search.presentation.MovieStateAdapter
import ter.den.feature_search.presentation.SearchViewModel
import javax.inject.Inject

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding ?: throw CustomThrowable.BindingNull

    private val movieAdapter by lazy {
        val onClickListener: ((Int, String) -> Unit) = { id, title ->
            findNavController().navigate(
                R.id.action_searchFragment_to_detailsFragment, bundleOf(
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
    private val viewModel: SearchViewModel
            by viewModels(factoryProducer = { viewModelFactory })


    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<SearchComponentViewModel>()
            .searchComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        initRvAdapter()
        initObservers()
        initSearchListener()
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
            if (state.refresh is LoadState.NotLoading) {
                binding.tvEmpty.isVisible =
                    (movieAdapter.itemCount == 0 && binding.etSearch.text?.isNotEmpty() == true)
            }
            if (state.refresh is LoadState.Error) {
                requireActivity().hideSoftKeyboard()
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
        viewModel.query
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach(::updateSearchQuery)
            .launchIn(lifecycleScope)

        repeatOnCreatedLifecycle {
            viewModel.movies.collectLatest(movieAdapter::submitData)
        }
    }

    private fun updateSearchQuery(searchQuery: String) {
        with(binding.etSearch) {
            if ((text?.toString() ?: "") != searchQuery) {
                setText(searchQuery)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun initSearchListener() {
        if (binding.etSearch.text.isNullOrEmpty()) {
            binding.etSearch.requestFocus()
            requireActivity().showSoftKeyboard()
        }
        binding.etSearch.textChanges()
            .debounce(300)
            .onEach {
                binding.tvEmpty.isVisible = false
                viewModel.setQuery(it.toString())
            }
            .launchIn(lifecycleScope)
    }

    private fun initClickListeners() {

    }

    override fun onResume() {
        super.onResume()
        binding.appBarSearch.setExpanded(true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}