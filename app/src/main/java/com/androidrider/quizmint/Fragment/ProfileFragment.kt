package com.androidrider.quizmint.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.androidrider.quizmint.Activity.SignInScreen
import com.androidrider.quizmint.Model.UserModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var dialog : Dialog
    var currentChance=0L
    private var isExpand = true



    // Open Gallery For Profile Image
    private var profileImageUrl: Uri? = null
    private val launchProfileGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            profileImageUrl = result.data?.data
            binding.profileImage.setImageURI(profileImageUrl)
            uploadImage(profileImageUrl, "profileImage")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container,false)
        // Inflate the layout for this fragment


        showDetail()//Calling Function
        getDataFromFirebase()//Calling Function
        showCoin()//Calling Functions
        showSpinChance()//Calling Functions
        showDialog()
        openBottomSheet()//Calling Functions
        setupLogoutButton()//Calling Functions


        // Set up profile image click listener
        binding.profileImage.setOnClickListener {
            launchGallery(launchProfileGalleryActivity)
            showDialog()
        }


        return binding.root
    }


// **********************************************  image code start **********************************************
    // Open Gallery For Image
    private fun launchGallery(callback: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        callback.launch(intent)
    }

    // Upload Image to Firebase Storage
    private fun uploadImage(imageUri: Uri?, folderName: String) {
        if (imageUri == null) return
dialog.dismiss()
        val storageRef = Firebase.storage.reference.child("$folderName/${UUID.randomUUID()}.jpg")
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                saveImageUriToDatabase(uri.toString())

                dialog.dismiss()
                Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Save Image URL to Firebase Realtime Database
    private fun saveImageUriToDatabase(imageUrl: String) {
        val userId = Firebase.auth.currentUser!!.uid
        val userRef = Firebase.database.reference.child("Users").child(userId)
        userRef.child("profileImageUrl").setValue(imageUrl)
    }

// **********************************************  image code end **********************************************



    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                Firebase.auth.signOut()
                startActivity(Intent(requireContext(), SignInScreen::class.java))
                requireActivity().finish() // Finish the hosting activity (MainActivity)
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
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

    private fun showSpinChance() {
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentChance=snapshot.getValue() as Long
                        binding.spinChance.text=(snapshot.getValue() as Long).toString()
                    }else{
                        var temp=0
                        binding.spinChance.text=temp.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
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

    private fun showDetail() {
        binding.imageButton.setOnClickListener {
            if (isExpand) {
                binding.expandableConstraintLayout.visibility = View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.ic_arrow_up)
            } else {
                binding.expandableConstraintLayout.visibility = View.GONE
                binding.imageButton.setImageResource(R.drawable.ic_arrow_down)
            }
            isExpand = !isExpand
        }
    }

    private fun getDataFromFirebase(){

        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var user = snapshot.getValue(UserModel::class.java)

                        binding.tvNameUp.text = user?.name
                        binding.tvName.text = user?.name
                        binding.tvEmail.text = user?.email
                        binding.tvPassword.text = user?.password

                        if (user?.profileImageUrl != null && user.profileImageUrl.isNotEmpty()) {
                            // If profile image URL exists, load it using Glide
                            Glide.with(this@ProfileFragment)
                                .load(user.profileImageUrl)
                                .into(binding.profileImage)
                        } else {
                            // If profile image URL is blank or null, set a placeholder or default image
                            binding.profileImage.setImageResource(R.drawable.img_male)
                        }

                        dialog.dismiss()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // TODO("Not yet implemented")
                    }
                }
            )
    }

}