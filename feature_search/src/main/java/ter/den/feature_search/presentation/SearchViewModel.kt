package ter.den.feature_search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ter.den.feature_search.domain.model.Movie
import ter.den.feature_search.domain.usecases.OnFavoriteStatusChangeUseCase
import ter.den.feature_search.domain.usecases.SearchUseCase
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val onFavoriteStatusChangeUseCase: OnFavoriteStatusChangeUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var newPagingSource: PagingSource<*, *>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies: StateFlow<PagingData<Movie>> = query
        .map(::newPager)
        .flatMapLatest { pager -> pager.flow }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    private fun newPager(query: String): Pager<Int, Movie> {
        return Pager(PagingConfig(5)) {
            searchUseCase(query).also { newPagingSource = it }
        }
    }

    fun setQuery(query: String) {
        _query.tryEmit(query)
    }

    fun onClickFavorite(id: Int) = viewModelScope.launch {
        onFavoriteStatusChangeUseCase(id.toString())
    }
}