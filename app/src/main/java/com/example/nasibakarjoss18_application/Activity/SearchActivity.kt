package com.example.nasibakarjoss18_application.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)

        updateBottomNavIcon(R.id.search)

        binding.bottomNav.selectedItemId = R.id.search
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




//        Navigate bottom setting
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == binding.bottomNav.selectedItemId) {
                binding.bottomNav.menu.findItem(R.id.search).icon =
                    ContextCompat.getDrawable(this, R.drawable.togglecolor)
                return@setOnItemSelectedListener true
            }


            when (item.itemId) {
                R.id.main -> startActivity(Intent(this, MainActivity::class.java))
                R.id.search -> startActivity(Intent(this, SearchActivity::class.java))
            }
            true
        }
    }

    private fun updateBottomNavIcon(activeItemId: Int) {
        val menu = binding.bottomNav.menu

        // Search
        menu.findItem(R.id.search).icon =
            ContextCompat.getDrawable(
                this,
                if (activeItemId == R.id.search)
                    R.drawable.togglecolor
                else
                    R.drawable.toggle
            )
    }
}