package com.example.nasibakarjoss18_application.Activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Fragment.BarangKeluarBottomSheet
import com.example.nasibakarjoss18_application.Fragment.BarangMasukBottomSheet
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.PopularViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityDetailBinding
import okhttp3.MultipartBody
import java.io.File

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: PopularViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        Handle button add barang masuk
        binding.tambahStockBtn.setOnClickListener {
            val bottomSheet = BarangMasukBottomSheet(intent.getStringExtra("id")!!)
            bottomSheet.show(supportFragmentManager, "BarangMasuk")
        }

        //        Handle button add barang keluar
        binding.kurangStockBtn.setOnClickListener {
            val bottomSheet = BarangKeluarBottomSheet(intent.getStringExtra("id")!!)
            bottomSheet.show(supportFragmentManager, "BarangKeluar")
        }

        initFormItem()
    }

    fun initFormItem () {
        var imgUrl : String = ""
        val pickImage =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    Glide.with(applicationContext).load(uri).into(binding.picItem)
                    viewModel.upload(this, uri)
                }
            }

        viewModel.imageUrl.observe(this){
            imgUrl = it.toString()
        }

        var popular : String = ""
        var jumlahBarang : Long = 0

//        show data config
        viewModel.itemResult.observe(this){
                data ->
            Log.d("data", data.toString())
            binding.apply {
                nameItemFormTxt.setText(data.nama.toString())
                descEdt.setText(data.deskripsi.toString())
                Glide.with(applicationContext).load(data.imgUrl).into(binding.picItem)

                gambarBarangForm.setOnClickListener {
                    pickImage.launch("image/*")
                }

            editBtn.setOnClickListener {
                viewModel.updateItem(
                    data.documentId,
                    nameItemFormTxt.text.toString().toLowerCase(),
                    descEdt.text.toString(),
                    jumlahBarang,
                    if (popular == "Populer") true else false,
                    if (imgUrl == "") data.imgUrl else imgUrl,
                )
            }

            }

            viewModel.updateStatus.observe(this){
                    success ->
                if (success) {
                    Toast.makeText(this@DetailActivity, "Update berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        viewModel.loadData(intent.getStringExtra("id")!!)

//    Setting drop down
        val items = listOf("Populer", "Tidak")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            items
        )

        if (binding.dropdownMenu.text.isNullOrEmpty()) {
            binding.dropdownLayout.error = "Harus dipilih"
        } else {
            binding.dropdownLayout.error = null
        }

        binding.dropdownMenu.setAdapter(adapter)

        binding.dropdownMenu.setOnItemClickListener { _, _, position, _ ->
            val selected = items[position]
            popular = selected
        }



        binding.backBtn.setOnClickListener {
            finish()
        }
    }

}