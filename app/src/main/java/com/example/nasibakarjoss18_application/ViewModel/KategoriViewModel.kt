package com.example.nasibakarjoss18_application.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasibakarjoss18_application.Domain.KategoriModel
import com.example.nasibakarjoss18_application.Repository.KategoriRepository

class KategoriViewModel : ViewModel() {
    private val repository = KategoriRepository()

    private val _kategoriState = MutableLiveData<MutableList<KategoriModel>>()
    val kategoriState = _kategoriState

    fun getKategori () {
        repository.getKategori() {
            Log.d("LISTDATA2", it.toString())
            _kategoriState.value = it
        }
    }


}