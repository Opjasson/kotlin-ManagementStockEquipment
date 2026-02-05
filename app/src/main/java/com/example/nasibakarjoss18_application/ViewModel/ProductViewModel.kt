package com.example.nasibakarjoss18_application.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasibakarjoss18_application.Domain.ProductModel
import com.example.nasibakarjoss18_application.Repository.CloudinaryRepository
import com.example.nasibakarjoss18_application.Repository.ProductRepository

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

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

    //    Create item
    val createStatus = MutableLiveData<Boolean>()

    fun createItem(
        nama_product: String,
        deskripsi_product: String,
        harga_product: Long,
        kategori_product: String,
        imgUrl: String,
        promo: Boolean,
    ) {
        repository.createItem(nama_product, deskripsi_product,harga_product, kategori_product,
            imgUrl, promo) {
                success ->
            if (success){
                createStatus.value = success
            }else {
                Log.d("FAILEDCREATE", "FAILED-CREATE ITEM")
            }
        }
    }

    //    Get all items
    private val _searchResult = MutableLiveData<List<ProductModel>>()
    val searchResult: LiveData<List<ProductModel>> = _searchResult

    fun loadAllItems() {
        repository.getAllItems() {
            _searchResult.value = it
        }
    }

    //    Delete product
    var _successDelete = MutableLiveData<Boolean>()

    fun deleteProduct(productId : String) {
        repository.deleteProduct(productId) {
            _successDelete.value = it
        }
    }

    //    get product by kategori
    private val _productKategoriResult = MutableLiveData<List<ProductModel>>()
    val productKategoriResult: LiveData<List<ProductModel>> = _productKategoriResult

    fun getProductByKategori(kategori : String) {
        repository.getProductByKategori(kategori) {
            _productKategoriResult.value = it
        }
    }

    //    Get product by offer

    private val _productOfferResult = MutableLiveData<List<ProductModel>>()
    val productOfferResult : LiveData<List<ProductModel>> = _productOfferResult

    fun getProductOffer() {
        repository.getProductOffer() {
            _productOfferResult.value = it
        }
    }

    //    get product by itemId
//    private val _productIdResult = MutableLiveData<ProductModel>()
//    val productIdResult: LiveData<ProductModel> = _productIdResult
//
//    fun getProductById(id : String) {
//        repository.getProductByProductId(id) {
//            _productIdResult.value = it
//        }
//    }

}