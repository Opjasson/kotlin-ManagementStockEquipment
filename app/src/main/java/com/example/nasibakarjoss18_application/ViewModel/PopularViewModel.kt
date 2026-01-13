package com.example.nasibakarjoss18_application.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.Repository.CloudinaryRepository
import com.example.nasibakarjoss18_application.Repository.PopularRepository
import com.google.firebase.firestore.FirebaseFirestore

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

//    Backend cloudinary
    private val repo = CloudinaryRepository()
    val imageUrl = MutableLiveData<String>()

    fun upload(context: Context, uri: Uri) {
        repo.uploadImageToCloudinary(
            context,
            uri,
            onSuccess = { imageUrl.postValue(it) },
            onError = { Log.d("ERROR", "Internal Error") }
        )
    }

//    Update item
    val updateStatus = MutableLiveData<Boolean>()

    fun updateItem(
        itemId : String,
        nama : String,
        deskripsi : String,
        jumlahBarang : Long,
        popular : Boolean,
        imgUrl : String,
    ) {
        repository.updateItem(itemId, nama, deskripsi, jumlahBarang, popular, imgUrl) {
            updateStatus.value = it
        }
    }

//    Get all items

}