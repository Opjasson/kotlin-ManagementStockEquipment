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

        binding.tambahStockBtn.setOnClickListener {
            val bottomSheet = BarangMasukBottomSheet()
            bottomSheet.show(supportFragmentManager, "BarangMasuk")
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

        var popular = ""

//        show data config
        viewModel.itemResult.observe(this){
                data ->
            Log.d("data", data.toString())
            binding.apply {
                nameItemFormTxt.setText(data.nama.toString())
                jumlahBarangForm.setText(data.jumlahBarang.toString())
                descEdt.setText(data.deskripsi.toString())
                descEdt.setText(data.deskripsi.toString())
                Glide.with(applicationContext).load(data.imgUrl).into(binding.picItem)

                plusBtn.setOnClickListener {
                    val current = jumlahBarangForm.text.toString().toIntOrNull() ?: 0
                    val newJumlah = current + 1
                    jumlahBarangForm.setText(newJumlah.toString())
                }
                minBtn.setOnClickListener {
                    val current = jumlahBarangForm.text.toString().toIntOrNull() ?: 0
                    val newJumlah = current - 1
                    jumlahBarangForm.setText(newJumlah.toString())
                }

                gambarBarangForm.setOnClickListener {
                    pickImage.launch("image/*")
                }

            editBtn.setOnClickListener {
                viewModel.updateItem(
                    data.documentId,
                    nameItemFormTxt.text.toString().toLowerCase(),
                    descEdt.text.toString(),
                    jumlahBarangForm.text.toString().toLongOrNull() ?: 0,
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