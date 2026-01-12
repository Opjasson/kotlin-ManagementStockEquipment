package com.example.nasibakarjoss18_application.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.Repository.PopularRepository

class PopularViewModel : ViewModel() {

//    Get item popular
private val repository = PopularRepository()
private val _popularResult = MutableLiveData<List<ItemsModel>>()
val popularResult : LiveData<List<ItemsModel>> = _popularResult
fun getPopularItem () {
    repository.getPopularItem() {
        Log.d("VM_DATA", "DATA MASUK VM: ${it.size}")
        _popularResult.value = it
    }
}
}