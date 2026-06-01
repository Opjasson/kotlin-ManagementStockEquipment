package com.example.nasibakarjoss18_application.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasibakarjoss18_application.Adapter.AlatMakanAdapter
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.PopularViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityNotifikasiBinding

class NotifikasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotifikasiBinding
    private lateinit var viewModel: PopularViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this)[PopularViewModel::class.java]

        updateBottomNavIcon(R.id.notif)
        binding.bottomNav.selectedItemId = R.id.notif

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initTotalStok()
        initAlatMakan()
        initAlatMasak()
        initAlatCuci()

        // Navigate bottom setting
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == binding.bottomNav.selectedItemId) {
                return@setOnItemSelectedListener true
            }

            when (item.itemId) {
                R.id.main -> startActivity(Intent(this, MainActivity::class.java))
                R.id.search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.notif -> startActivity(Intent(this, NotifikasiActivity::class.java))
                R.id.account -> startActivity(Intent(this, AccountActivity::class.java))
            }
            true
        }
    }

    private fun initTotalStok() {
        // Mengamati searchResult yang berisi semua item
        viewModel.searchResult.observe(this) { list ->
            val total = list.sumOf { it.jumlahBarang }
            binding.totalStokTxt.text = "Total Stok: $total"
        }
        viewModel.loadAllItems()
    }

    fun initAlatMakan() {
        val adapterAlatMakan = AlatMakanAdapter()

        binding.alatMakanView.apply {
            layoutManager = LinearLayoutManager(this@NotifikasiActivity, LinearLayoutManager.VERTICAL, false)
            adapter = adapterAlatMakan
        }

        viewModel.alatMakanResult.observe(this) { list ->
            binding.loadAlatMakan.visibility = View.GONE
            adapterAlatMakan.setData(list)
        }

        viewModel.getAlatMakan()
    }

    fun initAlatMasak() {
        val adapterAlatMasak = AlatMakanAdapter()

        binding.alatMasakView.apply {
            layoutManager = LinearLayoutManager(this@NotifikasiActivity, LinearLayoutManager.VERTICAL, false)
            adapter = adapterAlatMasak
        }

        viewModel.alatMasakResult.observe(this) { list ->
            binding.loadAlatMasak.visibility = View.GONE
            adapterAlatMasak.setData(list)
        }

        viewModel.getAlatMasak()
    }

    fun initAlatCuci() {
        val adapterAlatCuci = AlatMakanAdapter()

        binding.alatCuciView.apply {
            layoutManager = LinearLayoutManager(this@NotifikasiActivity, LinearLayoutManager.VERTICAL, false)
            adapter = adapterAlatCuci
        }

        viewModel.alatCuciResult.observe(this) { list ->
            binding.loadAlatCuci.visibility = View.GONE
            adapterAlatCuci.setData(list)
        }

        viewModel.getAlatCuci()
    }

    private fun updateBottomNavIcon(activeItemId: Int) {
        val menu = binding.bottomNav.menu
        menu.findItem(R.id.notif).icon = ContextCompat.getDrawable(
            this,
            if (activeItemId == R.id.notif) R.drawable.bellcolor else R.drawable.bell
        )
    }
}
