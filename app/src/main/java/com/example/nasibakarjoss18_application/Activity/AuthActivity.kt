package com.example.nasibakarjoss18_application.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nasibakarjoss18_application.Adapter.AuthPagerAdapter
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayoutMediator

class AuthActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setting tab nab
        val adapter = AuthPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ){
                tab, position ->
            tab.text = if (position == 0) {
                "Sign In"
            }else if (position == 1) {
                "Sign Up"
            }else {
                "Forgot Password"
            }
        }.attach()
    }

    fun moveToForgotPassword() {
        binding.viewPager.setCurrentItem(3, true)
    }
}