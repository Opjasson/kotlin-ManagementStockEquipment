package com.example.nasibakarjoss18_application.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasibakarjoss18_application.Domain.HistoryProductModel
import com.example.nasibakarjoss18_application.Domain.LaporanModel
import com.example.nasibakarjoss18_application.Domain.LaporanProductModel
import com.example.nasibakarjoss18_application.Domain.TransaksiWithCartModel
import com.example.nasibakarjoss18_application.Repository.TransaksiRepository
import com.example.nasibakarjoss18_application.Repository.UserRepository
import kotlinx.coroutines.launch

class TransaksiViewModel : ViewModel() {
    private val repository = TransaksiRepository()
    private val repositoryUser = UserRepository()
//    private var prefRepo = TransaksiPreference


    //    Create item
    val createStatus = MutableLiveData<String>()

    fun createTransaksi(
        userId: String,
        totalHarga: Long,
        catatanTambahan: String,
        buktiTransfer: String,
    ) {
        repository.createTransaksi(
            userId,
            totalHarga,
            catatanTambahan,
            buktiTransfer
        ) { success ->
            if (!success.isEmpty()) {
                createStatus.value = success
            } else {
                Log.d("FAILEDCREATE", "FAILED-CREATE ITEM")
            }
        }
    }

    //    Update Transaksi
    val updateStatus = MutableLiveData<Boolean>()

    fun updateTransaksi(
        transaksiId: String,
        totalHarga: Long,
        catatanTambahan: String,
        buktiTransfer: String,
    ) {
        repository.updateTransaksi(transaksiId, totalHarga, catatanTambahan, buktiTransfer) {
            updateStatus.value = it
        }
    }

    //    get transaksi to handle history transaksi
    private val _transaksiUI =
        MutableLiveData<List<TransaksiWithCartModel>>()

    val transaksiUI: LiveData<List<TransaksiWithCartModel>> =
        _transaksiUI

    fun loadTransaksiWithCart(userId: String) {
        viewModelScope.launch {
            val transaksiList = repository.getTransaksiByUser(userId)

            val result = transaksiList.map { (transaksiId, transaksi) ->

                val carts = repository.getCartHistoryTransaksi(transaksiId)

                val cartCustom = carts.mapNotNull { cart ->
                    val product = repository.getProductById(cart.productId)
                    val username = repositoryUser.getUsersByUserId(cart.userId)
                    product?.let {
                        HistoryProductModel(
                            username = username!!.username,
                            cartId = cart.documentId,
                            productId = cart.productId,
                            nama = it.nama_product,
                            harga = it.harga_product,
                            kategori = it.kategori_product,
                            jumlah = cart.jumlah,
                            imgUrl = it.imgUrl
                        )
                    }
                }

                TransaksiWithCartModel(
                    transaksiId = transaksiId,
                    transaksi = transaksi,
                    cartItems = cartCustom
                )
            }

            _transaksiUI.postValue(result)
        }
    }

    //    get transaksi to handle laporan
    private val _transaksiLaporan =
        MutableLiveData<List<LaporanModel>>()

    val transaksiLaporan: LiveData<List<LaporanModel>> =
        _transaksiLaporan

    fun loadTransaksiLaporan(tanggal1: String, tanggal2: String) {
        viewModelScope.launch {
            val transaksiList = repository.getTransaksiByDate(tanggal1, tanggal2)

            val result = transaksiList.map { (transaksiId, transaksi) ->

                val carts = repository.getCartHistoryTransaksi(transaksiId)

                val cartCustom = carts.mapNotNull { cart ->
                    val product = repository.getProductById(cart.productId)
                    product?.let {
                        LaporanProductModel(
                            createdAt = cart.createdAt,
                            cartId = cart.documentId,
                            productId = cart.productId,
                            nama = it.nama_product,
                            harga = it.harga_product,
                            kategori = it.kategori_product,
                            jumlah = cart.jumlah,
                            imgUrl = it.imgUrl
                        )
                    }
                }

                LaporanModel(
                    cartItems = cartCustom
                )
            }

            _transaksiLaporan.postValue(result)
        }
    }
}