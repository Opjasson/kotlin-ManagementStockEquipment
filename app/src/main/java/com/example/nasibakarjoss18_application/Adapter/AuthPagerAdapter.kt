package com.example.nasibakarjoss18_application.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nasibakarjoss18_application.Activity.AuthActivity
import com.example.nasibakarjoss18_application.Activity.MainActivity
import com.example.nasibakarjoss18_application.Fragment.ForgotPasswordFragment
import com.example.nasibakarjoss18_application.Fragment.SignInFragment
import com.example.nasibakarjoss18_application.Fragment.SignUpFragment
import com.example.nasibakarjoss18_application.databinding.FragmentSignInBinding

class AuthPagerAdapter(activity: AuthActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SignInFragment()
            1 -> SignUpFragment()
            else -> ForgotPasswordFragment()
        }
    }
}