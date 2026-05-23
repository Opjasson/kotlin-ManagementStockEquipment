package com.example.nasibakarjoss18_application.ViewModel

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasibakarjoss18_application.DataStore.UserPreference
import com.example.nasibakarjoss18_application.Repository.AuthRepository
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository()

    private val userPreference = UserPreference(application)
    
    fun saveUserIdToLocal() {
        val userId = repository.getCurrentUserId()
        if (userId != null) {
            viewModelScope.launch {
                userPreference.saveUserId(userId)
            }
        }
    }
    
    fun getUserId() = userPreference.getUserId().asLiveData()

    private val _loginState = MutableLiveData<Result<String>>()
    val loginState : LiveData<Result<String>> = _loginState

    fun loginUser (
        email : String,
        password : String,
    ) {
        repository.login(email, password) { success, message ->
            if (success) {
                _loginState.value = Result.success("Berhasil login")
            } else {
                _loginState.value = Result.failure(Exception(message))
            }
        }
    }

    private val _registState = MutableLiveData<Result<String>>()
    val registState : LiveData<Result<String>> = _registState

    fun registrasiUser (
        username : String,
        email : String,
        password : String,
    ) {
        repository.registrasi(username, email, password) { success, message ->
            if (success) {
                _registState.value = Result.success(message)
            } else {
                _registState.value = Result.failure(Exception(message))
            }
        }
    }

    fun logout () {
        repository.logout()
    }

    private val _lupaPassState = MutableLiveData<Boolean>()
    val lupaPassState : LiveData<Boolean> = _lupaPassState

    fun lupaPassword (email : String) {
        repository.lupaPassword(email) {
            _lupaPassState.value = it
        }
    }
}