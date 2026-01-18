package com.example.nasibakarjoss18_application.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasibakarjoss18_application.Domain.UsersModel
import com.example.nasibakarjoss18_application.Repository.UserRepository
import javax.security.auth.callback.Callback

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

//    Get userByUid
private val _userLoggin = MutableLiveData<UsersModel?>()
    val userLogin: LiveData<UsersModel?> = _userLoggin
fun getUserByUid () {
    repository.getUsersByUid() {
        result ->
        _userLoggin.value = result
    }
}

}