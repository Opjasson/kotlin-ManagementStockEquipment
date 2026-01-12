package com.example.nasibakarjoss18_application.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasibakarjoss18_application.Adapter.AuthPagerAdapter
import com.example.nasibakarjoss18_application.Adapter.KategoriAdapter
import com.example.nasibakarjoss18_application.Adapter.PopularAdapter
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.AuthViewModel
import com.example.nasibakarjoss18_application.ViewModel.KategoriViewModel
import com.example.nasibakarjoss18_application.ViewModel.PopularViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.api.Context

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel = KategoriViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)

        updateBottomNavIcon(R.id.main)

        binding.bottomNav.selectedItemId = R.id.main
        setContentView(binding.root)

        val kategoriAdapter = KategoriAdapter(mutableListOf())
        binding.kategoriView.adapter = kategoriAdapter

        viewModel.getKategori()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //        Navigate bottom setting
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == binding.bottomNav.selectedItemId) {
                binding.bottomNav.menu.findItem(R.id.main).icon =
                    ContextCompat.getDrawable(this, R.drawable.homecolor)
                return@setOnItemSelectedListener true
            }


            when (item.itemId) {
                R.id.main -> startActivity(Intent(this, MainActivity::class.java))
                R.id.search -> startActivity(Intent(this, SearchActivity::class.java))
            }
            true
        }

//        Tampilkan data dari viewModel kategori
        binding.loadKategori.visibility = View.VISIBLE
        viewModel.kategoriState.observe(this){
            list ->
            binding.kategoriView.layoutManager = LinearLayoutManager(this@MainActivity,
                LinearLayoutManager.HORIZONTAL,false
            )
            binding.kategoriView.adapter = KategoriAdapter(list)
            binding.loadKategori.visibility = View.GONE
        }

        authViewModel.getUserId().observe(this) { userId ->
            if (userId == "PZ2LOzb4IbTGW2ZqBnrEu7rgxBP2") {
                binding.admBtn.visibility = View.GONE
            }else {
                binding.admBtn.visibility = View.VISIBLE
            }
        }

        initPopular()
    }

//    Get Data Popular
    private fun initPopular() {
        var adapterPopular: PopularAdapter
        var viewModelPopular: PopularViewModel

            // 1. INIT VIEWMODEL
            viewModelPopular = ViewModelProvider(this)[PopularViewModel::class.java]

            // 2. INIT ADAPTER
            adapterPopular = PopularAdapter()

            // 3. SET RECYCLERVIEW
            binding.popularView.apply {
                layoutManager = GridLayoutManager(this@MainActivity, 2)
                adapter = adapterPopular
            }
            // 4. OBSERVE DATA
            viewModelPopular.popularResult.observe(this) { list ->
                binding.loadPopular.visibility = View.GONE
                adapterPopular.setData(list)
            }

            // 5. PANGGIL DATA
            viewModelPopular.getPopularItem()

    }



//    bottom Nav Setting
    private fun updateBottomNavIcon(activeItemId: Int) {
        val menu = binding.bottomNav.menu

        // Home
        menu.findItem(R.id.main).icon =
            ContextCompat.getDrawable(
                this,
                if (activeItemId == R.id.main)
                    R.drawable.homecolor
                else
                    R.drawable.home
            )

//        // Search
//        menu.findItem(R.id.search).icon =
//            ContextCompat.getDrawable(
//                this,
//                if (activeItemId == R.id.search)
//                    R.drawable.ic_search_active
//                else
//                    R.drawable.ic_search_inactive
//            )
//
//        // Profile
//        menu.findItem(R.id.profile).icon =
//            ContextCompat.getDrawable(
//                this,
//                if (activeItemId == R.id.profile)
//                    R.drawable.ic_profile_active
//                else
//                    R.drawable.ic_profile_inactive
//            )
    }
}