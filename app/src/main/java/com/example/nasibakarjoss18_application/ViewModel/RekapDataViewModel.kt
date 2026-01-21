package com.example.nasibakarjoss18_application.ViewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasibakarjoss18_application.Domain.BarangKeluarModel
import com.example.nasibakarjoss18_application.Domain.BarangMasukModel
import com.example.nasibakarjoss18_application.Domain.BarangRekapModel
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.Domain.StokAwalModel
import com.example.nasibakarjoss18_application.Repository.PopularRepository

class RekapDataViewModel : ViewModel() {
    private val barangRepo = PopularRepository()
    private val stokAwalRepo = PopularRepository()
    private val masukRepo = PopularRepository()
    private val keluarRepo = PopularRepository()

    private val barangLiveData = MutableLiveData<List<ItemsModel>>()
    private val awalLiveData = MutableLiveData<List<StokAwalModel>>()
    private val masukLiveData = MutableLiveData<List<BarangMasukModel>>()
    private val keluarLiveData = MutableLiveData<List<BarangKeluarModel>>()

    val rekapResult = MediatorLiveData<List<BarangRekapModel>>()

    init {
        combineData()
    }


    fun loadData(tanggal1: String, tanggal2: String) {
        barangRepo.getAllItems {
            barangLiveData.value = it
        }

        stokAwalRepo.getStokAwal(tanggal1, tanggal2) {
            awalLiveData.value = it
        }

        masukRepo.getBarangMasuk(tanggal1, tanggal2) {
            masukLiveData.value = it
        }

        keluarRepo.getBarangKeluar(tanggal1, tanggal2) {
            keluarLiveData.value = it
        }
    }

    private fun combineData() {

        rekapResult.addSource(barangLiveData) { buildRekap() }
        rekapResult.addSource(awalLiveData) { buildRekap() }
        rekapResult.addSource(masukLiveData) { buildRekap() }
        rekapResult.addSource(keluarLiveData) { buildRekap() }
    }

    private fun buildRekap() {

        val barangList = barangLiveData.value ?: return
        val awalList = awalLiveData.value ?: emptyList()
        val masukList = masukLiveData.value ?: emptyList()
        val keluarList = keluarLiveData.value ?: emptyList()


        val masukMap: Map<String, Int> =
            masukList
                .filter { it.barangId != null }
                .groupBy { it.barangId!! }
                .mapValues { it.value.sumOf { m -> m.barang_masuk.toInt() } }

        val awalMap: Map<String, Int> =
            awalList
                .filter { it.barangId != null }
                .groupBy { it.barangId!! }
                .mapValues { it.value.sumOf { m -> m.jumlah.toInt() } }

        val keluarMap: Map<String, Int> =
            keluarList
                .filter { it.barangId != null }
                .groupBy { it.barangId!! }
                .mapValues { it.value.sumOf { m -> m.barang_keluar.toInt() } }

        val result = barangList.map { barang ->
            BarangRekapModel(
                barangId = barang.documentId,
                namaBarang = barang.nama,
                stokAwal = awalMap[barang.documentId] ?: 0,
                totalMasuk = masukMap[barang.documentId] ?: 0,
                totalKeluar = keluarMap[barang.documentId] ?: 0
            )
        }

        rekapResult.value = result
    }
}