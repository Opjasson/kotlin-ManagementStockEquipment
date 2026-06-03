package com.example.nasibakarjoss18_application.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nasibakarjoss18_application.Adapter.ItemsAdapter
import com.example.nasibakarjoss18_application.Adapter.PopularAdapter
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.PopularViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityItemByKategoriBinding


class ItemByKategoriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemByKategoriBinding
    private val viewModel = PopularViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityItemByKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initItems()

    }

    //    Inisialisasi item
    private fun initItems() {
        val itemsAdapter : PopularAdapter
        itemsAdapter = PopularAdapter()

        binding.kategoriTxt.text = intent.getStringExtra("nama")!!

        binding.itemsKategoriView.layoutManager = GridLayoutManager(this@ItemByKategoriActivity, 2)

        binding.itemsKategoriView.adapter = itemsAdapter

        binding.backItemKategoriBtn.setOnClickListener {
            finish()
        }
        viewModel.alatMakanAllResult.observe(this) {
            list ->
            Log.d("ALL", list.size.toString())
            binding.loadItems.visibility = View.GONE
            binding.jmlItemTxt.text = list.size.toString()
            itemsAdapter.setData(list)
        }
        viewModel.getAlatMakanAll(intent.getLongExtra("id", 1)!!)

    }
}