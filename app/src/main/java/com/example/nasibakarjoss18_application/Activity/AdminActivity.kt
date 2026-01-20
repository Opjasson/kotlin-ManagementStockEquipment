package com.example.nasibakarjoss18_application.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nasibakarjoss18_application.Adapter.PopularAdapter
import com.example.nasibakarjoss18_application.Domain.BarangMasukModel
import com.example.nasibakarjoss18_application.Domain.BarangRekapModel
import com.example.nasibakarjoss18_application.Domain.HargaBBM
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.PopularViewModel
import com.example.nasibakarjoss18_application.ViewModel.RekapDataViewModel
import com.example.nasibakarjoss18_application.ViewModel.UserViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityAdminBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    val viewModel = PopularViewModel()
    val viewModelRekap = RekapDataViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initFormItem()

        val etDateRange = binding.etDateRange

        etDateRange.setOnClickListener {
            showDateRangePicker(etDateRange)
        }

    }

    var imgUrl : String = ""


    fun initFormItem () {
        val pickImage =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                Log.d("URL", uri.toString())
                uri?.let {
                    Glide.with(applicationContext).load(uri).into(binding.ADMPicItem)
                    viewModel.upload(this, uri)
                }
            }

        viewModel.imageUrl.observe(this){
            imgUrl = it.toString()
        }

        var popular = ""
        var kategoriId : String = ""

//        show data config
        binding.plusBtn.setOnClickListener {
            val current = binding.ADMJumlahBarangForm.text.toString().toIntOrNull() ?: 0
            val newJumlah = current + 1
            binding.ADMJumlahBarangForm.setText(newJumlah.toString())
        }
        binding.minBtn.setOnClickListener {
            val current = binding.ADMJumlahBarangForm.text.toString().toIntOrNull() ?: 0
            val newJumlah = current - 1
            binding.ADMJumlahBarangForm.setText(newJumlah.toString())
        }

        binding.ADMPicItem.visibility = View.GONE
        binding.gambarBarangBtn.setOnClickListener {
            binding.ADMPicItem.visibility = View.VISIBLE
            pickImage.launch("image/*")
        }

        viewModel.createStatus.observe(this){
                success ->
            if (success) {
                Toast.makeText(this, "Data berhasil dibuat", Toast.LENGTH_SHORT).show()
                finish()
            }
        }


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

        //    Setting drop down 2
        val items2 = listOf("Alat Makan", "Alat Cuci", "Alat Masak")

        val adapter2 = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            items2
        )

        if (binding.dropdownMenu2.text.isNullOrEmpty()) {
            binding.dropdownLayout2.error = "Harus dipilih"
        } else {
            binding.dropdownLayout2.error = null
        }

        binding.dropdownMenu2.setAdapter(adapter2)

        binding.dropdownMenu2.setOnItemClickListener { _, _, position, _ ->
            val selected2 = items2[position]
            kategoriId = selected2

            var kategoriId2 : Long = 0

            if (kategoriId == "Alat Makan") {
                kategoriId2 = 2
            }else if (kategoriId == "Alat Cuci") {
                kategoriId2 = 1
            }else {
                kategoriId2 = 0
            }

            binding.addItemBtn.setOnClickListener {
                viewModel.createItem(
                    binding.ADMNamaItemForm.text.toString().toLowerCase(),
                    binding.ADMDescEdt.text.toString(),
                    binding.ADMJumlahBarangForm.text.toString().toLongOrNull() ?: 0,
                    if (popular == "Populer") true else false,
                    imgUrl,
                    kategoriId2,
                )
            }

        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun showDateRangePicker(editText: TextInputEditText) {

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Pilih Rentang Tanggal")
            .build()

        picker.show(supportFragmentManager, "DATE_RANGE_PICKER")

        picker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

            val start = formatter.format(Date(startDate))
            val end = formatter.format(Date(endDate))

            editText.setText("$start  -  $end")
            Log.d("DATE", "start : ${start} - ${end}")


            val table = binding.tableHarga

            viewModelRekap.loadData(start, end)

            viewModelRekap.rekapResult.observe(this) {
                list ->
                Log.d("DATANEW", list.toString())

                // 1️⃣ HAPUS SEMUA ROW DATA (kecuali header)
                val childCount = table.childCount
                if (childCount > 1) {
                    table.removeViews(1, childCount - 1)
                }
                Log.d("LISTBARANGMASUK", list.toString())

                binding.cetakBtn.setOnClickListener {
                    generatePdf(this, list)
                }


                list.forEachIndexed { index, item ->
                    val row = TableRow(this)
                    row.setBackgroundColor(Color.WHITE)

                    row.addView(createCell((index + 1).toString(), Gravity.CENTER))
                    row.addView(createCell(item.namaBarang.toString(), Gravity.CENTER))
                    row.addView(createCell(item.totalMasuk.toString(), Gravity.START))
                    row.addView(createCell(item.totalMasuk.toString(), Gravity.START))
                    row.addView(createCell(item.totalKeluar.toString(), Gravity.START))
                    row.addView(createCell(item.totalKeluar.toString(), Gravity.START))

                    table.addView(row)
                }
            }


        }
    }


    private fun createCell(text: String, gravity: Int): TextView {
        return TextView(this).apply {
            this.text = text
            this.gravity = gravity
            setPadding(8, 8, 8, 8)
            setBackgroundResource(android.R.drawable.editbox_background)
        }
    }

    fun generatePdf(context: Context, data: List<BarangRekapModel>) {

        val pdfDocument = PdfDocument()

        val textPaint = TextPaint().apply {
            textSize = 12f
            isAntiAlias = true
        }

        val titlePaint = Paint().apply {
            textSize = 16f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        // Ukuran A4 (pt)
        val pageWidth = 595
        val pageHeight = 842

        // Layout table
        val startX = 30f
        val startY = 100f
        val rowHeight = 28f
        val bottomMargin = 800f

        // Kolom X
        val colNo = startX
        val colNama = startX + 40
        val colAwal = startX + 180
        val colMasuk = startX + 270
        val colKeluar = startX + 360
        val colAkhir = startX + 450

        fun drawTextSafe(
            canvas: Canvas,
            text: String,
            x: Float,
            y: Float,
            maxWidth: Float
        ) {
            val safeText = TextUtils.ellipsize(
                text,
                textPaint,
                maxWidth,
                TextUtils.TruncateAt.END
            ).toString()

            canvas.drawText(safeText, x, y, textPaint)
        }

        fun drawHeader(canvas: Canvas, y: Float) {
            drawTextSafe(canvas, "No", colNo, y, 30f)
            drawTextSafe(canvas, "Nama", colNama, y, 120f)
            drawTextSafe(canvas, "Jumlah Awal", colAwal, y, 70f)
            drawTextSafe(canvas, "Masuk", colMasuk, y, 70f)
            drawTextSafe(canvas, "Keluar", colKeluar, y, 70f)
            drawTextSafe(canvas, "Akhir", colAkhir, y, 70f)

            canvas.drawLine(20f, y + 5f, pageWidth - 20f, y + 5f, linePaint)
        }

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // Judul
        canvas.drawText("Laporan Data Barang Masuk", 40f, 50f, titlePaint)

        var y = startY
        drawHeader(canvas, y)
        y += rowHeight

        data.forEachIndexed { index, item ->

            // ➡️ Pindah halaman
            if (y > bottomMargin) {
                pdfDocument.finishPage(page)

                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas

                canvas.drawText("Laporan Data Barang Masuk", 40f, 50f, titlePaint)

                y = startY
                drawHeader(canvas, y)
                y += rowHeight
            }

            drawTextSafe(canvas, (index + 1).toString(), colNo, y, 30f)
            drawTextSafe(canvas, item.namaBarang.toString(), colNama, y, 120f)
            drawTextSafe(canvas, item.totalMasuk.toString(), colAwal, y, 70f)
            drawTextSafe(canvas, item.totalMasuk.toString(), colMasuk, y, 70f)
            drawTextSafe(canvas, item.totalKeluar.toString(), colKeluar, y, 70f)
            drawTextSafe(canvas, item.totalKeluar.toString(), colAkhir, y, 70f)

            y += rowHeight
        }

        pdfDocument.finishPage(page)

        // Simpan file
        val file = File(context.getExternalFilesDir(null), "laporan_barang_masuk.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        Toast.makeText(context, "PDF berhasil dibuat", Toast.LENGTH_SHORT).show()

        // Buka PDF
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        context.startActivity(intent)
    }

}