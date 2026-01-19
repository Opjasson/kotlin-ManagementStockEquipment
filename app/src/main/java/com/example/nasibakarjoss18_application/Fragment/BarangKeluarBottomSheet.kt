package com.example.nasibakarjoss18_application.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.nasibakarjoss18_application.R
import com.example.nasibakarjoss18_application.ViewModel.PopularViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BarangKeluarBottomSheet.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarangKeluarBottomSheet(private var barangId : String
) : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel = PopularViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.bottomsheet_barang_keluar,
            container,
            false
        )
        val etJumlah = view.findViewById<EditText>(R.id.etJumlahKeluar)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpanBrgKeluar)
//

        viewModel.itemResult.observe(this){
                data ->
            btnSimpan.setOnClickListener {
                val jumlah = etJumlah.text.toString().toLong()
                viewModel.updateItem(
                    barangId,
                    data.nama.toString(),
                    data.deskripsi.toString(),
                    data.jumlahBarang.toLong() - jumlah,
                    data.popular,
                    data.imgUrl.toString()
                )
                viewModel.addBarangKeluarItem(barangId,jumlah)
                Toast.makeText(requireContext(), "Stock dikurangi", Toast.LENGTH_SHORT).show()
                dismiss()
            }

        }
        viewModel.loadData(barangId)

        return view
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment BarangKeluarBottomSheet.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            BarangKeluarBottomSheet().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}