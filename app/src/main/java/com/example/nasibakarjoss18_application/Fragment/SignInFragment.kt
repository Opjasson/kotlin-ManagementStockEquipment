package com.example.nasibakarjoss18_application.Fragment

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.databinding.FragmentSignInBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onViewCreated(view : View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        binding.apply {
            showBtn.visibility = View.GONE
            LAlertForm.visibility = View.GONE

//            Toggle visibility password
            hideBtn.setOnClickListener {
                showBtn.visibility = View.VISIBLE
                hideBtn.visibility = View.GONE

                LPasswordFormTxt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            showBtn.setOnClickListener {
                hideBtn.visibility = View.VISIBLE
                showBtn.visibility = View.GONE
                LPasswordFormTxt.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            loginBtn.setOnClickListener {
                var email = LEmailFormTxt.text.toString().trim()
                var password = LPasswordFormTxt.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    LAlertForm.text = "Oops..,formulir belum terisi"
                    LAlertForm.visibility = View.VISIBLE
                }
            }


        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 🚨 WAJIB (hindari memory leak)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}