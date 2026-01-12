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

//    get popular item view model
private val _popularResult = MutableLiveData<List<ItemsModel>>()
val popularResult : LiveData<List<ItemsModel>> = _popularResult
fun getPopularItem () {
    repository.getPopularItem() {
        _popularResult.value = it
    }
}

//    get item by itemId
private val _itemResult = MutableLiveData<List<ItemsModel>>()
    val itemResult: LiveData<List<ItemsModel>> = _itemResult

    fun loadData(id : Long) {
        repository.getItemByItemId(id) {
            _itemResult.value = it
        }
    }
}