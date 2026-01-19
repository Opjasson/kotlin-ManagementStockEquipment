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
private val _itemResult = MutableLiveData<ItemsModel>()
    val itemResult: LiveData<ItemsModel> = _itemResult

    fun loadData(id : String) {
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
            onSuccess = {
                Log.d("imgUrlView", it.toString())
                imageUrl.postValue(it)
                        },
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
            Log.d("updateStatus", it.toString())
            updateStatus.value = it
        }
    }

//    Create item
val createStatus = MutableLiveData<Boolean>()

    fun createItem(
        nama : String,
        deskripsi : String,
        jumlahBarang : Long,
        popular : Boolean,
        imgUrl : String,
        kategoriId : Long
    ) {
        repository.createItem(nama, deskripsi, popular, imgUrl, kategoriId) {
            createStatus.value = it
        }
    }

    //    Add barang_masuk
    val addBarangStatus = MutableLiveData<Boolean>()

    fun addBarangItem(
        barangId : String,
        barang_masuk : Long,
    ) {
        repository.addStockItem(barangId, barang_masuk) {
            addBarangStatus.value = it
        }
    }


    //    Add barang_keluar
    val addBarangKeluarStatus = MutableLiveData<Boolean>()

    fun addBarangKeluarItem(
        barangId : String,
        barang_keluar : Long,
    ) {
        repository.minStockItem(barangId, barang_keluar) {
            addBarangKeluarStatus.value = it
        }
    }



//    Get all items
private val _searchResult = MutableLiveData<List<ItemsModel>>()
    val searchResult: LiveData<List<ItemsModel>> = _searchResult

    fun searchValue(keyword : String) {
        if (keyword.isEmpty()) {
            loadAllItems()
        } else {
            loadBySearchItem(keyword)
        }
    }

    fun loadAllItems() {
        repository.getAllItems() {
            _searchResult.value = it
        }
    }

    fun loadBySearchItem(keyword: String) {
        repository.searchItems(keyword) {
            _searchResult.value = it
        }
    }

//    Get item alat makan <= 3

    private val _alatMakanResult = MutableLiveData<List<ItemsModel>>()
    val alatMakanResult: LiveData<List<ItemsModel>> = _alatMakanResult

    fun getAlatMakan() {
        repository.getItemAlatMakan() {
            _alatMakanResult.value = it
        }
    }

    //    Get item alat makan all

    private val _alatMakanAllResult = MutableLiveData<List<ItemsModel>>()
    val alatMakanAllResult: LiveData<List<ItemsModel>> = _alatMakanAllResult

    fun getAlatMakanAll(kategoriId : Long) {
        repository.getItemAlatMakanAll(kategoriId) {
            Log.d("DATAALATMAKAN", it.toString())
            _alatMakanAllResult.value = it
        }
    }

//    Get item alat masak <= 3

    private val _alatMasakResult = MutableLiveData<List<ItemsModel>>()
    val alatMasakResult: LiveData<List<ItemsModel>> = _alatMasakResult

    fun getAlatMasak() {
        repository.getItemAlatMasak() {
            _alatMasakResult.value = it
        }
    }

    //    Get item alat cuci <= 3

    private val _alatCuciResult = MutableLiveData<List<ItemsModel>>()
    val alatCuciResult: LiveData<List<ItemsModel>> = _alatCuciResult

    fun getAlatCuci() {
        repository.getItemAlatCuci() {
            _alatCuciResult.value = it
        }
    }

    //    Create new item



}