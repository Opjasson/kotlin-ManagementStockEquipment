package com.example.nasibakarjoss18_application.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Domain.ProductModel
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.ProductViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityEditProductBinding

class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductBinding
    private var viewModel = ProductViewModel()
    private lateinit var drawerLayout: DrawerLayout
    private var imgUrl: String = ""
    private var kategori: String = ""
    private var promoSelected: Boolean = false
    private lateinit var product: ProductModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        product = intent.getSerializableExtra("object") as ProductModel

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initData()
        initFormUpdate()
        initSideBar()
    }

    private fun initData() {
        binding.nameItemFormTxt.setText(product.nama_product)
        binding.hargaItemFormTxt.setText(product.harga_product.toString())
        binding.stokItemFormTxt.setText(product.stok_product.toString())
        binding.descEdt.setText(product.deskripsi_product)
        imgUrl = product.imgUrl
        kategori = product.kategori_product
        promoSelected = product.promo

        Glide.with(this).load(imgUrl).into(binding.picItem)
        binding.dropdownMenu.setText(kategori, false)
        binding.dropdownMenu2.setText(if (promoSelected) "Ya" else "Tidak", false)
    }

    private fun initFormUpdate() {
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Glide.with(applicationContext).load(uri).into(binding.picItem)
                viewModel.upload(this, uri)
            }
        }

        viewModel.imageUrl.observe(this) {
            imgUrl = it.toString()
        }

        binding.gambarBarangForm.setOnClickListener {
            pickImage.launch("image/*")
        }

        viewModel.updateStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Dropdown Kategori
        val itemsKategori = listOf("minuman", "makanan")
        val adapterKategori = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, itemsKategori)
        binding.dropdownMenu.setAdapter(adapterKategori)
        binding.dropdownMenu.setOnItemClickListener { _, _, position, _ ->
            kategori = itemsKategori[position]
        }

        // Dropdown Promo
        val itemsPromo = listOf("Ya", "Tidak")
        val adapterPromo = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, itemsPromo)
        binding.dropdownMenu2.setAdapter(adapterPromo)
        binding.dropdownMenu2.setOnItemClickListener { _, _, position, _ ->
            promoSelected = itemsPromo[position] == "Ya"
        }

        binding.updateProductBtn.setOnClickListener {
            val nama = binding.nameItemFormTxt.text.toString()
            val harga = binding.hargaItemFormTxt.text.toString()
            val stok = binding.stokItemFormTxt.text.toString()
            val desc = binding.descEdt.text.toString()

            if (nama.isEmpty() || harga.isEmpty() || stok.isEmpty() || kategori.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.updateItem(
                product.documentId,
                nama.lowercase(),
                desc,
                harga.toLong(),
                kategori,
                imgUrl,
                promoSelected,
                stok.toLong()
            )
        }
    }

    private fun initSideBar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        drawerLayout = binding.drawerLayout
        val navigationView = binding.navigationView
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> startActivity(Intent(this, CashierActivity::class.java))
                R.id.menu_manageProduct -> startActivity(Intent(this, ManageProductActivity::class.java))
                R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
                R.id.menu_history -> startActivity(Intent(this, HistoryPesananActivity::class.java))
                R.id.menu_laporan -> startActivity(Intent(this, LaporanPenjualanActivity::class.java))
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}
