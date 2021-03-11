package com.example.scanfood.application.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

sealed class LoginViewModelState(
        open val errorMessage: String = "",
        open val loginButtonEnabled: Boolean = false
    ) {
        object Loading : LoginViewModelState()
        object Success : LoginViewModelState()
        data class Failure(override val errorMessage: String) :
            LoginViewModelState(errorMessage = errorMessage, loginButtonEnabled = true)

        data class UpdateLogin(override val loginButtonEnabled: Boolean) :
            LoginViewModelState(loginButtonEnabled = loginButtonEnabled)
}

class LoginViewModel : ViewModel() {
    private val state = MutableLiveData<LoginViewModelState>()
    fun getState(): LiveData<LoginViewModelState> = state

    fun login(username: String, password: String) {
        val isValid = validLogin(username, password)
        if (isValid) state.value =
            LoginViewModelState.Success else state.value =
            LoginViewModelState.Failure(
                "Invalid credentials"
            )
    }

    private fun validLogin(username: String, password: String): Boolean = username == "kotlin" && password == "rocks"
    fun updateLogin(username: String, password: String) {
        val buttonEnabled = username.isNotBlank() && password.isNotBlank()
        state.value =
            LoginViewModelState.UpdateLogin(
                loginButtonEnabled = buttonEnabled
            )
    }

}

