package com.example.nasibakarjoss18_application.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.PopularViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: PopularViewModel by viewModels()

    //    variable put extra
    private var id: Long = 0
    private var nama: String = ""

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

        initFormItem()
        getBundles()
    }

    fun initFormItem () {
        viewModel.itemResult.observe(this){
                data ->
            binding.apply {
                nameItemFormTxt.setText(data[0].nama.toString())
                jumlahBarangForm.setText(data[0].jumlahBarang.toString())

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
            }
        }
        viewModel.loadData(id)

        val items = listOf("Makanan", "Minuman", "Snack")

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
            Log.d("DROPDOWN", selected)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun getBundles() {
        //            Get context from put extra
        id = intent.getLongExtra("id",0)!!
        nama = intent.getStringExtra("nama")!!
    }
}