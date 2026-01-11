package com.example.nasibakarjoss18_application.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.Repository.PopularRepository

class PopularViewModel : ViewModel() {

//    Get item popular
private val _popularResult = MutableLiveData<List<ItemsModel>>()
val popularResult = LiveData<List<ItemsModel>> =_popularResult

   private val repository = PopularRepository()
fun getPopularItem () {
    repository.getPopularItem() {
        _popularResult.value = it
    }
}
}