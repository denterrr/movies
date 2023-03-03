package ter.den.feature_movie_details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ter.den.core.domain.model.CustomThrowable
import ter.den.core.domain.model.SingleLiveEvent
import ter.den.feature_movie_details.domain.model.Movie
import ter.den.feature_movie_details.domain.usecases.GetMovieDetailsUseCase
import ter.den.feature_movie_details.domain.usecases.OnFavoriteStatusChangeUseCase
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val onFavoriteStatusChangeUseCase: OnFavoriteStatusChangeUseCase,
) : ViewModel() {

    private var _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private var currentId: Int? = null

    private var _error = SingleLiveEvent<String>()
    val error: SingleLiveEvent<String> = _error

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CustomThrowable.Error) {
            _error.value = throwable.errorMessage
        } else _error.value = throwable.localizedMessage
    }

    fun setMovie(movieId: Int? = null) = viewModelScope.launch(errorHandler) {
        currentId = currentId ?: movieId
        val movie = getMovieDetailsUseCase(
            currentId ?: movieId ?: 0
        )
        _movie.value = movie
    }

    fun onFavoriteClick(id: Int) = viewModelScope.launch {
        onFavoriteStatusChangeUseCase(id.toString())
    }
}