package ter.den.core.domain.model

sealed class CustomThrowable : Throwable() {
    data class Error(val errorMessage: String) : CustomThrowable()
    object RetrofitNull : CustomThrowable()
    object BindingNull : CustomThrowable()
    object MovieNull : CustomThrowable()
    object SessionIdNull : CustomThrowable()
    object AccountDetailsNull : CustomThrowable()

}