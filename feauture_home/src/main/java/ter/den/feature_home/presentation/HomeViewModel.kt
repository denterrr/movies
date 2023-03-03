package ter.den.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ter.den.core.presentation.emptyErrorHandler
import ter.den.feature_home.domain.model.Popular
import ter.den.feature_home.domain.usecases.GetPopularContentUseCase
import ter.den.feature_home.domain.usecases.GetPopularFromDbUseCase
import ter.den.feature_home.domain.usecases.OnFavoriteStatusChangeUseCase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HomeViewModel @Inject constructor(
    private val getPopularContentUseCase: GetPopularContentUseCase,
    private val getPopularFromDbUseCase: GetPopularFromDbUseCase,
    private val onFavoriteStatusChangeUseCase: OnFavoriteStatusChangeUseCase,
) : ViewModel() {

    var popularContent: StateFlow<PagingData<Popular>>? = null

    suspend fun getPopularContent(): StateFlow<PagingData<Popular>> = suspendCoroutine {
        viewModelScope.launch {
            val dbData = getPopularFromDbUseCase()
            if (popularContent == null) popularContent = Pager(PagingConfig(pageSize = 5)) {
                getPopularContentUseCase()
            }.flow
                .cachedIn(viewModelScope)
                .stateIn(
                    viewModelScope,
                    SharingStarted.Lazily,
                    PagingData.from(dbData)
                )
            it.resume(
                (popularContent ?: emptyFlow()) as StateFlow<PagingData<Popular>>
            )
        }
    }

    fun onClickFavorite(id: Int) = viewModelScope.launch(emptyErrorHandler) {
        onFavoriteStatusChangeUseCase(id.toString())
    }

}