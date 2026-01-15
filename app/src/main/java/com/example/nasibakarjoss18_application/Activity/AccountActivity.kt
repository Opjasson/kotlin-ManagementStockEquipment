package com.example.nasibakarjoss18_application.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nasibakarjoss18_application.DataStore.UserPreference
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.AuthViewModel
import com.example.nasibakarjoss18_application.ViewModel.UserViewModel
import com.example.nasibakarjoss18_application.databinding.ActivityAccountBinding
import kotlinx.coroutines.launch

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private val userViewModel = UserViewModel()
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreference = UserPreference(this)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        updateBottomNavIcon(R.id.account)
        binding.bottomNav.selectedItemId = R.id.account
        setContentView(binding.root)

        userViewModel.getUserByUid()

        userViewModel.userLogin.observe(this) { user ->
            Log.d("USERlogin", user.toString())
            user?.let {
                binding.usernameValueTxt.setText(it.username.toString())
                binding.emailValueTxt.setText(it.email.toString())
                binding.roleValueTxt.setText(it.role.toString())
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.logoutUserBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
                userPreference.deleteUserId()

                val intent = Intent(this@AccountActivity, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            }

        }

        //        Navigate bottom setting
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == binding.bottomNav.selectedItemId) {
                binding.bottomNav.menu.findItem(R.id.account).icon =
                    ContextCompat.getDrawable(this, R.drawable.usercolor)
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

    //    bottom Nav Setting
    private fun updateBottomNavIcon(activeItemId: Int) {
        val menu = binding.bottomNav.menu

        // Home
        menu.findItem(R.id.account).icon =
            ContextCompat.getDrawable(
                this,
                if (activeItemId == R.id.account)
                    R.drawable.usercolor
                else
                    R.drawable.user
            )

    }
}