package com.androidrider.quizmint.Fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidrider.quizmint.Adapter.HistoryAdapter
import com.androidrider.quizmint.Model.HistoryModel
import com.androidrider.quizmint.Model.UserModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.FragmentHistoryBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Collections

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var dialog : Dialog
    private var historyList = ArrayList<HistoryModel>()
    private lateinit var adapter: HistoryAdapter

    private lateinit var radioGroupFilter: RadioGroup
    private var filterType = "ALL" // Default filter type

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentHistoryBinding.inflate(inflater, container,false)
        // Inflate the layout for this fragment

        getHistoryData()//Calling Function
        openBottomSheet()//Calling Function
        getDataFromFirebase()//Calling Function
        showCoin()//Calling Function
        navigateToProfileFragment()
        showDialog()

        initViews()
        setupRadioGroup()


        return binding.root
    }


    private fun initViews() {
        radioGroupFilter = binding.radioGroupFilter
    }

    private fun setupRadioGroup() {
        radioGroupFilter.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton: RadioButton = group.findViewById(checkedId)
            filterHistoryData(selectedRadioButton.text.toString())
        }
    }

    private fun filterHistoryData(filterType: String) {
        val filteredList = when (filterType) {
            "Wins" -> historyList.filter { !it.isWithdrawal }
            "Withdrawals" -> historyList.filter { it.isWithdrawal }
            else -> historyList
        }
        adapter.updateList(filteredList)
    }



    private fun navigateToProfileFragment() {

        binding.profileImage.setOnClickListener {
            val profileFragment = ProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, profileFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.tvName.setOnClickListener {
            val profileFragment = ProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, profileFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    private fun showDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun getHistoryData() {
        Firebase.database.reference.child("PlayerCoinHistory").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    historyList.clear()
                    var historyList1 = ArrayList<HistoryModel>()
                    for (datasnapshot in snapshot.children){
                        var data=datasnapshot.getValue(HistoryModel::class.java)
                        historyList1.add(data!!)
                    }
                    Collections.reverse(historyList1)
                    historyList.addAll(historyList1)
                    filterHistoryData("All") // Apply initial filter after data fetch
                    adapter.notifyDataSetChanged()


                    dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
                }

            })
    }

    private fun openBottomSheet() {
        binding.iconCoinWithdrawal.setOnClickListener {
            val bottomSheetDialog = WithdrawalFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        binding.textCoinWithdrawal.setOnClickListener {
            val bottomSheetDialog = WithdrawalFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
    }

    private fun showCoin() {
        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var currentCoin=snapshot.getValue() as Long
                        binding.textCoinWithdrawal.text=currentCoin.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        adapter = HistoryAdapter(historyList)
        binding.recyclerView.adapter=adapter
        binding.recyclerView.setHasFixedSize(true)


    }

    private fun getDataFromFirebase(){
        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var user = snapshot.getValue(UserModel::class.java)

                        binding.tvName.text = user?.name

                        if (user?.profileImageUrl != null) {
                            Glide.with(requireContext()).load(user.profileImageUrl)
                                .into(binding.profileImage)
                        }

                    }
                    override fun onCancelled(error: DatabaseError) {
                        // TODO("Not yet implemented")
                    }
                }
            )
    }




}