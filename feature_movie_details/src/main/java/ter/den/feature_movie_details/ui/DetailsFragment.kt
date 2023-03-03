package ter.den.feature_movie_details.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ter.den.core.domain.MovieDB
import ter.den.core.domain.model.CustomThrowable
import ter.den.core.presentation.ViewModelFactory
import ter.den.feature_movie_details.R
import ter.den.feature_movie_details.databinding.FragmentDetailsBinding
import ter.den.feature_movie_details.di.DetailsComponentViewModel
import ter.den.feature_movie_details.domain.model.Movie
import ter.den.feature_movie_details.presentation.DetailsViewModel
import javax.inject.Inject

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding ?: throw CustomThrowable.BindingNull

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: DetailsViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<DetailsComponentViewModel>()
            .detailsComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentDetailsBinding.inflate(layoutInflater)
        parseParams()
        return binding.root
    }

    private fun parseParams() {
        val movieId = arguments?.getInt(MOVIE_ID_KEY)
        viewModel.setMovie(movieId)
        val title = arguments?.getString(MOVIE_TITLE_KEY, "")
        binding.toolBarDetails.title = title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.movie.observe(viewLifecycleOwner) {
            binding.progress.isVisible = false
            binding.svMain.isVisible = true
            setMovieContent(it)
        }
        viewModel.error.observe(viewLifecycleOwner) { errorText ->
            binding.llError.isVisible = true
            binding.progress.isVisible = false
            binding.svMain.isVisible = false
            binding.btnRetry.isEnabled = true
            binding.tvError.text = errorText
        }
    }

    private fun setMovieContent(movie: Movie) {
        Glide.with(binding.root)
            .load("${MovieDB.IMAGE_BASE_URL}${movie.poster_path}")
            .into(binding.ivPoster)
        binding.tvDate.text = movie.release_date
        binding.tvRate.text = movie.vote_average.toString()
        binding.tvGenresAndRuntime.text =
            getString(R.string.genres_and_runtime).format(
                movie.genres.joinToString(separator = ", ") { it.name },
                movie.runtime.toString()
            )
        binding.tvOverview.text = movie.overview
        binding.ivRate.isVisible = true
        binding.tvOverviewHint.isVisible = true
        if (movie.isFavorite) binding.ivFavorite.setColorFilter(Color.RED)
        else binding.ivFavorite.clearColorFilter()

        binding.ivFavorite.setOnClickListener {
            movie.isFavorite = !movie.isFavorite
            viewModel.onFavoriteClick(movie.id)
            if (movie.isFavorite) binding.ivFavorite.setColorFilter(Color.RED)
            else binding.ivFavorite.clearColorFilter()
        }
    }

    private fun initClickListeners() {
        binding.toolBarDetails.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnRetry.setOnClickListener {
            binding.llError.isVisible = false
            binding.progress.isVisible = true
            viewModel.setMovie()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val MOVIE_ID_KEY = "MOVIE_ID_KEY"
        const val MOVIE_TITLE_KEY = "MOVIE_TITLE_KEY"
    }
}