package com.example.nasibakarjoss18_application.Activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.drawToBitmap
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasibakarjoss18_application.Adapter.LaporanAdapter
import com.example.nasibakarjoss18_application.DataStore.UserPreference
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.AuthViewModel
import com.example.nasibakarjoss18_application.ViewModel.TransaksiViewModel
import com.example.nasibakarjoss18_application.ViewModel.UserViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityLaporanPenjualanBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class LaporanPenjualanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaporanPenjualanBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: TransaksiViewModel
    private lateinit var adapter: LaporanAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var authViewModel: AuthViewModel
    private val formatter = NumberFormat.getInstance(Locale("id", "ID"))
    private val userViewModel = UserViewModel()

    private var dateFrom: String = ""
    private var dateTo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLaporanPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel dan Preference
        viewModel = ViewModelProvider(this)[TransaksiViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userPreference = UserPreference(this)
        drawerLayout = binding.drawerLayout

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Memuat data user
        userViewModel.getUserByUid()

        setupRecyclerView()
        setupDatePickers()
        setupObservers()
        initSideBar()

        binding.btnFilter.setOnClickListener {
            if (dateFrom.isEmpty() || dateTo.isEmpty()) {
                Toast.makeText(this, "Pilih rentang tanggal terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            viewModel.loadTransaksiLaporan(dateFrom, dateTo)
        }

        binding.btnPrintReport.setOnClickListener {
            val bitmap = binding.layoutLaporan.drawToBitmap()
            val pdfFile = createTempPdf(this, bitmap)
            previewPdf(this, pdfFile)
        }
    }

    private fun setupRecyclerView() {
        adapter = LaporanAdapter(emptyList())
        binding.rvLaporan.layoutManager = LinearLayoutManager(this)
        binding.rvLaporan.adapter = adapter
    }

    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()

        binding.tvDateFrom.setOnClickListener {
            DatePickerDialog(this, { _, year, month, day ->
                val selectedDate = String.format("%04d/%02d/%02d", year, month + 1, day)
                dateFrom = selectedDate
                binding.tvDateFrom.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.tvDateTo.setOnClickListener {
            DatePickerDialog(this, { _, year, month, day ->
                val selectedDate = String.format("%04d/%02d/%02d", year, month + 1, day)
                dateTo = selectedDate
                binding.tvDateTo.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setupObservers() {
        viewModel.transaksiLaporan.observe(this) { reports ->
            binding.progressBar.visibility = View.GONE
            
            val allItems = reports.flatMap { it.cartItems }
            adapter.updateData(allItems)

            val grandTotal = allItems.sumOf { it.harga * it.jumlah }
            val formattedTotal = "Rp ${formatter.format(grandTotal)}"
            
            binding.tvTotalPenjualan.text = formattedTotal
            binding.tvTotalLaporan.text = formattedTotal
            binding.tvPeriod.text = "Periode: $dateFrom - $dateTo"
            
            if (allItems.isEmpty()) {
                Toast.makeText(this, "Tidak ada data pada periode ini", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun previewPdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)
    }

    private fun createTempPdf(context: Context, bitmap: Bitmap): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)
        val file = File(context.cacheDir, "laporan_penjualan.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()
        return file
    }

    private fun initSideBar () {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val navigationView = binding.navigationView

        // Logic untuk menyembunyikan menu berdasarkan role
        userViewModel.userLogin.observe(this) { user ->
            if (user?.role == "kasir") {
                navigationView.menu.findItem(R.id.menu_manageProduct).isVisible = false
            }
        }

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
                R.id.menu_history -> {
                    startActivity(Intent(this, HistoryPesananActivity::class.java))
                }
                R.id.menu_laporan -> {
                    drawerLayout.closeDrawers()
                }
                R.id.menu_logout -> {
                    performLogout()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun performLogout() {
        lifecycleScope.launch {
            authViewModel.logout()
            userPreference.deleteUserId()

            val intent = Intent(this@LaporanPenjualanActivity, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}
