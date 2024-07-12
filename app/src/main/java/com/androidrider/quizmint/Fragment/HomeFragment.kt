package com.androidrider.quizmint.Fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.androidrider.quizmint.Adapter.CategoryAdapter
import com.androidrider.quizmint.Model.SubjectModel
import com.androidrider.quizmint.Model.UserModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialog : Dialog

    private var categoryList = ArrayList<SubjectModel>()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container,false)


        setupRecyclerView()

        openBottomSheet()//Calling Function
        getDataFromFirebase() //Calling Function
        showCoin()//Calling Function
        getDataInRecyclerView()//Calling Function
        showDialog()//Calling Function

//        navigateToProfileFragment()

        return binding.root
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

    private fun showDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun getDataInRecyclerView() {

        Firebase.firestore.collection("Subject")
            .get().addOnSuccessListener {
                categoryList.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(SubjectModel::class.java)
                    if (data != null) {
                        categoryList.add(data)
                    }
                }
                categoryAdapter.updateList(categoryList)
                dialog.dismiss()
            }
    }


    private fun getDataFromFirebase(){
        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue(UserModel::class.java)
                        binding.tvName.text = user?.name

                        if (user?.profileImageUrl != null && user.profileImageUrl.isNotEmpty()) {
                            // If profile image URL exists, load it using Glide
                            Glide.with(this@HomeFragment)
                                .load(user.profileImageUrl)
                                .into(binding.profileImage)
                        } else {
                            // If profile image URL is blank or null, set a placeholder or default image
                            binding.profileImage.setImageResource(R.drawable.img_male)
                        }

                    }
                    override fun onCancelled(error: DatabaseError) {
                        // TODO("Not yet implemented")
                    }
                }
            )
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        categoryAdapter = CategoryAdapter(requireContext(), categoryList)
        binding.recyclerView.adapter = categoryAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCategories(newText ?: "")
                return true
            }
        })
    }

    private fun filterCategories(query: String) {
        val filteredList = categoryList.filter {
            it.subject!!.contains(query, ignoreCase = true)
        }
        categoryAdapter.updateList(filteredList)
    }


}