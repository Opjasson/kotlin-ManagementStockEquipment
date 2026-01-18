package com.example.nasibakarjoss18_application.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
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
import com.example.nasibakarjoss18_application.Domain.HargaBBM
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.PopularViewModel
import com.example.nasibakarjoss18_application.ViewModel.UserViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityAdminBinding
import java.io.File
import java.io.FileOutputStream

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    val viewModel = PopularViewModel()

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
        initTable()

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

    fun initTable () {
        val table = binding.tableHarga

        val data = listOf(
            HargaBBM(1, "Pertamax Turbo", "9,850"),
            HargaBBM(2, "Pertamax", "9,000"),
            HargaBBM(3, "Pertalite", "7,650"),
            HargaBBM(4, "Premium", "6,450"),
            HargaBBM(5, "Pertamina Dex", "10,200"),
            HargaBBM(6, "Dexlite", "9,500"),
            HargaBBM(7, "Bio Solar", "9,400")
        )

        binding.cetakBtn.setOnClickListener {
            generatePdf(this, data)
        }

        for (item in data) {
            val row = TableRow(this)
            row.setBackgroundColor(Color.WHITE)

            row.addView(createCell(item.no.toString(), Gravity.CENTER))
            row.addView(createCell(item.nama, Gravity.START))
            row.addView(createCell(item.harga, Gravity.END))

            table.addView(row)
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

    fun generatePdf(context: Context, data: List<HargaBBM>) {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titlePaint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Judul
        titlePaint.textSize = 16f
        titlePaint.isFakeBoldText = true
        canvas.drawText("Laporan Data User", 40f, 50f, titlePaint)

        paint.textSize = 12f

        // Header tabel
        var y = 100f
        canvas.drawText("No", 40f, y, paint)
        canvas.drawText("Nama", 200f, y, paint)
        canvas.drawText("Harga", 450f, y, paint)

        y += 20f

        // Isi tabel
        for (user in data) {
            canvas.drawText(user.no.toString(), 40f, y, paint)
            canvas.drawText(user.nama, 200f, y, paint)
            canvas.drawText(user.harga.toString(), 450f, y, paint)
            y += 20f
        }

        pdfDocument.finishPage(page)

        // Simpan file
        val file = File(context.getExternalFilesDir(null), "laporan_user.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        Toast.makeText(context, "PDF berhasil dibuat", Toast.LENGTH_SHORT).show()

        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(intent)
    }
}