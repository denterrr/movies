package ter.den.feature_profile.presentation

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ter.den.core.domain.model.CustomThrowable
import ter.den.core.domain.model.SingleLiveEvent
import ter.den.feature_profile.domain.model.AccountDetails
import ter.den.feature_profile.domain.model.Movie
import ter.den.feature_profile.domain.usecases.*
import javax.inject.Inject
import javax.inject.Provider

class ProfileViewModel @Inject constructor(
    private val getSessionIdFlowUseCase: GetSessionIdFlowUseCase,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getWatchListUseCase: GetWatchListUseCase,
    private val onFavoriteStatusChangeUseCase: OnFavoriteStatusChangeUseCase,
    private val createSessionIdUseCase: CreateSessionIdUseCase,
) : ViewModel() {

    private val _throwableMessage = SingleLiveEvent<String>()
    val throwableMessage: LiveData<String> = _throwableMessage

    var favoriteContent: StateFlow<PagingData<Movie>> =
        MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    var watchListContent: StateFlow<PagingData<Movie>> =
        MutableStateFlow<PagingData<Movie>>(PagingData.empty())

    private val _accountDetails = MutableLiveData<AccountDetails?>()
    val accountDetails: LiveData<AccountDetails?> = _accountDetails

    init {
        setUpProfile()
    }

    private fun setUpProfile() = viewModelScope.launch {
        val sessionId = getSessionIdFlowUseCase().stateIn(viewModelScope).value
        if (sessionId.isNullOrEmpty()) _accountDetails.value = null
        else {
            favoriteContent = Pager(PagingConfig(pageSize = 5)) {
                getFavoritesUseCase(sessionId ?: throw CustomThrowable.SessionIdNull)
            }.flow
                .cachedIn(viewModelScope)
                .stateIn(
                    viewModelScope,
                    SharingStarted.Lazily,
                    PagingData.empty()
                )

            watchListContent = Pager(PagingConfig(pageSize = 5)) {
                getWatchListUseCase(sessionId ?: throw CustomThrowable.SessionIdNull)
            }.flow
                .cachedIn(viewModelScope)
                .stateIn(
                    viewModelScope,
                    SharingStarted.Lazily,
                    PagingData.empty()
                )
            _accountDetails.value = getAccountDetailsUseCase(sessionId)
        }
    }

    fun onFavoriteClick(id: Int) = viewModelScope.launch {
        onFavoriteStatusChangeUseCase(id.toString())
    }

    private val loginErrorHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CustomThrowable.Error) _throwableMessage.value = throwable.errorMessage
        else _throwableMessage.value = throwable.toString()
    }

    fun tryLogin(login: String, password: String) = viewModelScope.launch(loginErrorHandler) {
        createSessionIdUseCase(login, password)
        setUpProfile()
    }
}

internal class ViewModelFactory @Inject constructor(
    private val viewModelProviders:
    @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModelProviders[modelClass]?.get() as T
}