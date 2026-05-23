package com.example.nasibakarjoss18_application.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Adapter.CardProductListCartAdapter
import com.example.nasibakarjoss18_application.DataStore.TransaksiPreference
import com.example.nasibakarjoss18_application.DataStore.UserPreference
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.CartViewModel
import com.example.nasibakarjoss18_application.ViewModel.ProductViewModel
import com.example.nasibakarjoss18_application.ViewModel.TransaksiViewModel
import com.example.nasibakarjoss18_application.ViewModel.UserViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityCartBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private val viewModel = CartViewModel()
    private val viewModelTransaksi = TransaksiViewModel()
    private val userViewModel = UserViewModel()
    private val viewModelImg = ProductViewModel()
    private lateinit var drawerLayout: DrawerLayout
    private val prefRepo = TransaksiPreference(this)
    private lateinit var userPreference: UserPreference

    private var imgUrlProof: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initHandleBuy()
        initSideBar()
    }

    private fun initSideBar() {
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
                R.id.menu_home -> startActivity(Intent(this, CashierActivity::class.java))
                R.id.menu_manageProduct -> startActivity(Intent(this, ManageProductActivity::class.java))
                R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
                R.id.menu_history -> startActivity(Intent(this, HistoryPesananActivity::class.java))
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun initHandleBuy() {
        // Image Picker for Proof of Transfer

        binding.buktiTfImg.visibility = View.GONE

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.buktiTfImg.visibility = View.VISIBLE
                Glide.with(applicationContext).load(uri).into(binding.buktiTfImg)
                viewModelImg.upload(this, uri)
            }
        }

        viewModelImg.imageUrl.observe(this) {
            imgUrlProof = it.toString()
            Toast.makeText(this, "Bukti transfer berhasil diunggah", Toast.LENGTH_SHORT).show()
        }

        binding.buktiTfBtn.setOnClickListener {
            pickImage.launch("image/*")
        }

        lifecycleScope.launch {
            val transId = prefRepo.getTransactionId().first()
            viewModel.getCartByTransaksiId(transId.toString())
        }

        binding.loadCart.visibility = View.VISIBLE
        viewModel.cartResult.observe(this) { list ->
            if (list.isEmpty()) {
                binding.loadCart.visibility = View.GONE
                return@observe
            }
            
            viewModel.loadCartCustom(list)

            viewModel.transaksiUI.observe(this) { data ->
                val totalHarga = data.sumOf { it.harga * it.jumlah }
                binding.tvTotal.text = "Rp $totalHarga"

                binding.etNominalBayar.addTextChangedListener { s ->
                    val input = s.toString().toLongOrNull() ?: 0L
                    val kembalian = input - totalHarga
                    if (kembalian >= 0) {
                        binding.tvKembalian.text = "Rp $kembalian"
                    } else {
                        binding.tvKembalian.text = "Rp 0"
                    }
                }

                binding.btnBuy.setOnClickListener {
                    val nominalBayar = binding.etNominalBayar.text.toString().toLongOrNull() ?: 0L
                    
                    if (nominalBayar < totalHarga) {
                        Toast.makeText(this, "Nominal bayar tidak mencukupi!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // For transfer payment, ensure image is uploaded
                    // If nominal is exactly total or user wants to save proof
                    // Here we save both
                    viewModelTransaksi.updateTransaksi(
                        list[0].transaksiId,
                        totalHarga.toLong(),
                        nominalBayar,
                        "", // catatanTambahan (bisa dikosongkan)
                        imgUrlProof
                    )

                    viewModelTransaksi.updateStatus.observe(this) { success ->
                        if (success) {
                            Toast.makeText(this, "Pesanan Berhasil Dibayar", Toast.LENGTH_SHORT).show()
                            lifecycleScope.launch {
                                prefRepo.clearTransactionId()
                                finish()
                            }
                        }
                    }
                }

                binding.rvCart.layoutManager = LinearLayoutManager(this)
                binding.rvCart.adapter = CardProductListCartAdapter(
                    onKurangClick = { cart ->
                        if (cart.jumlah > 1) viewModel.minusQtyCart(cart.cartId)
                    },
                    onPlusClick = { cart ->
                        viewModel.addQtyCart(cart.cartId)
                    },
                    data.toMutableList(),
                )
                binding.loadCart.visibility = View.GONE
            }
        }
    }
}