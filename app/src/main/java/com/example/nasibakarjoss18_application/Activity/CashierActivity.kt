package com.example.nasibakarjoss18_application.Activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasibakarjoss18_application.Adapter.CardProductListAdapter
import com.example.nasibakarjoss18_application.DataStore.TransaksiPreference
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.CartViewModel
import com.example.nasibakarjoss18_application.ViewModel.ProductViewModel
import com.example.nasibakarjoss18_application.ViewModel.TransaksiViewModel
import com.example.nasibakarjoss18_application.ViewModel.UserViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityCashierBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.toString

class CashierActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    private val viewModelTransaksi = TransaksiViewModel()

    private val userViewModel = UserViewModel()
    private val viewModelCart = CartViewModel()
    private val viewModel = ProductViewModel()

    private val prefRepo = TransaksiPreference(this)
    private lateinit var binding : ActivityCashierBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCashierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initSideBar()
        initShowProduct()
    }

    private fun initShowProduct () {
        userViewModel.getUserByUid()

        userViewModel.userLogin.observe(this) { user ->
            user?.let {
                val headerView = binding.navigationView.getHeaderView(0)



                headerView.findViewById<TextView>(R.id.tvNameHeader).text = user?.username
                headerView.findViewById<TextView>(R.id.tvEmailHeader).text = user?.email

                val menu = binding.navigationView.menu
                if (user?.documentId != "JTER5kKcDvRerpk6c9pJYGxhd7D2") {
                    menu.findItem(R.id.menu_laporan)?.isVisible = false
                    menu.findItem(R.id.menu_manageProduct)?.isVisible = false
                }
            }



            //      create transaksi
            binding.transaksiBtn.setOnClickListener {

                viewModelTransaksi.createTransaksi(
                    user?.documentId.toString(),
                    0,
                    "",
                    ""
                )

                viewModelTransaksi.createStatus.observe(this) {
                        documentId ->
                    lifecycleScope.launch {
                        prefRepo.saveTransactionId(documentId)
                    }
                    Toast.makeText(this, "Silahkan melanjutkan pesanan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        var kategori : String = "makanan"
        viewModel.getProductByKategori(kategori)

////        Button kategori makanan handle
//        binding.makananBtn.setOnClickListener {
//            kategori = "makanan"
//            binding.makananBtn.backgroundTintList = ColorStateList
//                .valueOf(ContextCompat
//                    .getColor(this, R.color.btnon))
//
//            binding.makananBtn.setTextColor(
//                ContextCompat
//                    .getColor(this, R.color.white)
//            )
//
//            binding.minumanBtn.backgroundTintList = ColorStateList
//                .valueOf(ContextCompat
//                    .getColor(this, R.color.btnoff))
//
//            binding.minumanBtn.setTextColor(
//                ContextCompat
//                    .getColor(this, R.color.black)
//            )
//            viewModel.getProductByKategori(kategori)
//        }

////        Button kategori minuman handle
//        binding.minumanBtn.setOnClickListener {
//            kategori = "minuman"
//            binding.makananBtn.backgroundTintList = ColorStateList
//                .valueOf(ContextCompat
//                    .getColor(this, R.color.btnoff))
//
//            binding.makananBtn.setTextColor(
//                ContextCompat
//                    .getColor(this, R.color.black)
//            )
//
//            binding.minumanBtn.backgroundTintList = ColorStateList
//                .valueOf(ContextCompat
//                    .getColor(this, R.color.btnon))
//
//            binding.minumanBtn.setTextColor(
//                ContextCompat
//                    .getColor(this, R.color.white)
//            )
//
//            viewModel.getProductByKategori(kategori)
//        }


        val productAdapter = CardProductListAdapter(
            onAddToCart = {
                    productId ->

                userViewModel.userLogin.observe(this) { user ->
                    lifecycleScope.launch {
                        val transaksiId = prefRepo.getTransactionId().first()

                        if (transaksiId.isNullOrBlank()) {
                            Toast.makeText(
                                this@CashierActivity,
                                "Buat Transaksi Dulu!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }

                        viewModelCart.addOrUpdateCart(
                            userId = user!!.documentId!!,
                            transaksiId = transaksiId,
                            productId = productId,
                            qty = 1
                        )

                        delay(500)
                        startActivity(Intent(this@CashierActivity, CartActivity::class.java))
                    }

                }

            },
            mutableListOf()
        )
        binding.rvMenu.adapter = productAdapter


        viewModel.loadAllItems()

        viewModel.searchResult.observe(this) {
                list ->
            binding.rvMenu.layoutManager = GridLayoutManager(this@CashierActivity, 2)
            binding.loadMenu.visibility = View.GONE

            productAdapter.updateData(list.toMutableList())
        }
    }

    private fun initSideBar () {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout

        val navigationView = binding.navigationView

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, CashierActivity::class.java))
                }
                R.id.menu_manageProduct -> {
                    startActivity(Intent(this, ManageProductActivity::class.java))
                }
                R.id.menu_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                }
//                R.id.menu_history -> {
//                    startActivity(Intent(this, HistoryTransaksiActivity::class.java))
//                }
//                R.id.menu_laporan -> {
//                    startActivity(Intent(this, LaporanTransactionActivity::class.java))
//                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}