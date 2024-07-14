package com.androidrider.quizmint.Fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.androidrider.quizmint.Model.HistoryModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.FragmentWithdrawalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class WithdrawalFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentWithdrawalBinding

    var currentCoin = 0L
    var totalAmountBeforeWithdrawal = 0.0 // Total amount in rupees before withdrawal


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWithdrawalBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        fetchCurrentCoin()//Calling Function
        withdrawCoin()//Calling Function
        showCoin()//Calling Function

        return binding.root
    }

    private fun showCoin() {
        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var currentCoin = snapshot.getValue() as Long

                        binding.tvTotalCoin.text = currentCoin.toString()

                        updateTotalAmountBeforeWithdrawal() // Update total amount before withdrawal
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    private fun fetchCurrentCoin() {
        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        currentCoin = snapshot.getValue() as Long
                        updateTotalAmountBeforeWithdrawal() // Update total amount before withdrawal

                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    private fun withdrawCoin() {
        binding.btnTransferAmount.setOnClickListener {

            val withdrawalAmount = binding.edtAmount.text.toString().toDouble()
            val coinsToDeduct = withdrawalAmount * 100 // 100 coins for every 1 rupees


            if (coinsToDeduct <= currentCoin) {
                Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentCoin - coinsToDeduct)

                var deductedCoin=coinsToDeduct.toInt()

                val historyModel = HistoryModel(deductedCoin.toString(), withdrawalAmount.toString(),
                    System.currentTimeMillis().toString(),true
                )
                Firebase.database.reference.child("PlayerCoinHistory")
                    .child(Firebase.auth.currentUser!!.uid)
                    .push().setValue(historyModel)

                // Show the custom dialog
                val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
                val imageView = dialogView.findViewById<ImageView>(R.id.imageView)
                val textViewDetails = dialogView.findViewById<TextView>(R.id.textViewDetails)
                val btnOk = dialogView.findViewById<Button>(R.id.btnOk)

                imageView.setImageResource(R.drawable.img_success)
                val amount = binding.edtAmount.text.toString()
                val upiId = binding.edtUpiId.text.toString()
                textViewDetails.text =
                    "Amount: $amount Rs. on UPI ID: $upiId"

                val dialog = AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .create()

                btnOk.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()

                binding.edtAmount.text.clear()
                binding.edtUpiId.text.clear()

            } else {
                Toast.makeText(requireContext(), "Insufficient Coin", Toast.LENGTH_SHORT).show()
            }
            // Dismiss the BottomSheetDialogFragment
            this@WithdrawalFragment.dismiss()
        }
    }

    private fun updateTotalAmountBeforeWithdrawal() {
        // Calculate total amount in rupees before withdrawal
        totalAmountBeforeWithdrawal = currentCoin / 100.0
        binding.tvTotalAmount.text = "$totalAmountBeforeWithdrawal Rs."
    }





}










//private fun showCoin() {
//    Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
//        .addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    var currentCoin = snapshot.getValue() as Long
//
//                    binding.tvTotalCoin.text = currentCoin.toString()
//
//                    updateTotalAmountBeforeWithdrawal() // Update total amount before withdrawal
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })
//}
//
//private fun fetchCurrentCoin() {
//    Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
//        .addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    currentCoin = snapshot.getValue() as Long
//                    updateTotalAmountBeforeWithdrawal() // Update total amount before withdrawal
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })
//}
//
//private fun withdrawCoin() {
//    binding.btnTransferAmount.setOnClickListener {
//
//        val withdrawalAmount = binding.edtAmount.text.toString().toDouble()
//        val coinsToDeduct = withdrawalAmount * 100 // 100 coins for every 10 rupees
//
//
//        if (coinsToDeduct <= currentCoin) {
//            Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
//                .setValue(currentCoin - coinsToDeduct)
//
//            val historyModel = HistoryModel(coinsToDeduct.toString(), withdrawalAmount.toString(),
//                System.currentTimeMillis().toString(),true
//            )
//            Firebase.database.reference.child("PlayerCoinHistory")
//                .child(Firebase.auth.currentUser!!.uid)
//                .push().setValue(historyModel)
//
//            // Show the custom dialog
//            val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
//            val imageView = dialogView.findViewById<ImageView>(R.id.imageView)
//            val textViewDetails = dialogView.findViewById<TextView>(R.id.textViewDetails)
//            val btnOk = dialogView.findViewById<Button>(R.id.btnOk)
//
//            imageView.setImageResource(R.drawable.img_success)
//            val amount = binding.edtAmount.text.toString()
//            val upiId = binding.edtUpiId.text.toString()
//            textViewDetails.text =
//                "Amount: $amount on UPI ID: $upiId\n\nCalculated Amount: $totalAmountBeforeWithdrawal Rs"
//
//            val dialog = AlertDialog.Builder(requireContext())
//                .setView(dialogView)
//                .create()
//
//            btnOk.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            dialog.show()
//
//            binding.edtAmount.text.clear()
//            binding.edtUpiId.text.clear()
//
//        } else {
//            Toast.makeText(requireContext(), "Insufficient Coin", Toast.LENGTH_SHORT).show()
//        }
//        // Dismiss the BottomSheetDialogFragment
//        this@WithdrawalFragment.dismiss()
//    }
//}
//
//private fun updateTotalAmountBeforeWithdrawal() {
//    // Calculate total amount in rupees before withdrawal
//    totalAmountBeforeWithdrawal = currentCoin / 100.0
//    binding.tvTotalAmount.text = "Total Amount : $totalAmountBeforeWithdrawal Rs."
//}



























//    private fun fetchCurrentCoin() {
//        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()){
//                        currentCoin=snapshot.getValue() as Long
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {}
//            })
//    }
//
//    private fun withdrawCoin() {
//        binding.btnTransferAmount.setOnClickListener {
//
//            if (binding.edtAmount.text.toString().toDouble()<=currentCoin){
//                Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
//                    .setValue(currentCoin-binding.edtAmount.text.toString().toDouble())
//
//                var historyModel= HistoryModel(binding.edtAmount.text.toString(),System.currentTimeMillis().toString() ,true)
//                Firebase.database.reference.child("PlayerCoinHistory").child(Firebase.auth.currentUser!!.uid)
//                    .push().setValue(historyModel)
//
//
//                // Show the custom dialog
//                val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
//                val imageView = dialogView.findViewById<ImageView>(R.id.imageView)
//                val textViewDetails = dialogView.findViewById<TextView>(R.id.textViewDetails)
//                val btnOk = dialogView.findViewById<Button>(R.id.btnOk)
//
//                imageView.setImageResource(R.drawable.img_success)
//                val amount = binding.edtAmount.text.toString()
//                val upiId = binding.edtUpiId.text.toString()
//                textViewDetails.text = "Amount: $amount on UPI ID: $upiId"
//
//                val dialog = AlertDialog.Builder(requireContext())
//                    .setView(dialogView)
//                    .create()
//
//                btnOk.setOnClickListener {
//                    dialog.dismiss()
//                }
//
//                dialog.show()
//
//
//                binding.edtAmount.text.clear()
//                binding.edtUpiId.text.clear()
//
//            }else{
//                Toast.makeText(requireContext(), "Insufficient Coin", Toast.LENGTH_SHORT).show()
//            }
//
//
//            // Dismiss the BottomSheetDialogFragment
//            this@WithdrawalFragment.dismiss()
//        }
//    }
//
//    private fun showCoin() {
//        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
//            .addValueEventListener(object :ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()){
//                        var currentCoin=snapshot.getValue() as Long
//                        binding.tvTotalCoin.text=currentCoin.toString()
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {}
//            })
//    }

