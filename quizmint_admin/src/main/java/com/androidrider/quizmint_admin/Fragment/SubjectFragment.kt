package com.androidrider.quizmint_admin.Fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import com.androidrider.quizmint_admin.Model.SubjectModel
import com.androidrider.quizmint_admin.R
import com.androidrider.quizmint_admin.Adapter.SubjectAdapter
import com.androidrider.quizmint_admin.databinding.FragmentSubjectBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class SubjectFragment : Fragment() {

    lateinit var binding: FragmentSubjectBinding

    private lateinit var dialog : Dialog
    private var imageUrl : Uri? = null
    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            imageUrl = it.data!!.data
            binding.imageBox.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubjectBinding.inflate(layoutInflater)

        // Access the toolbar view - Show/Hide
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.visibility = GONE

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)

        binding.apply {

            imageBox.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }

            uploadSubjectButton.setOnClickListener {
                validateData(binding.edtSubjectName.text.toString())
            }
        }

        getDataInRecycleView()

        return binding.root
    }

    private fun getDataInRecycleView() {

        val list = ArrayList<SubjectModel>()

        Firebase.firestore.collection("Subject")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(SubjectModel::class.java)
                    list.add(data!!)
                }
                binding.recyclerView.adapter = SubjectAdapter(requireContext(), list)
            }
    }


    private fun validateData(subjectName: String) {

        if (subjectName.isEmpty()){
            Toast.makeText(requireContext(), "please provide category name", Toast.LENGTH_SHORT).show()
        }else if(imageUrl == null){
            Toast.makeText(requireContext(), "please select image", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage(subjectName!!)
        }
    }

    private fun uploadImage(subjectName: String) {
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("Subject/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->

                    storeData(subjectName, image.toString())
                }
            }

            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with storage!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(subjectName: String, image: String) {

        val db = Firebase.firestore

        val data = hashMapOf<String, Any>(
            "subject" to subjectName,
            "img" to image
        )

        db.collection("Subject").add(data)
            .addOnSuccessListener {
                dialog.dismiss()

                getDataInRecycleView()

                binding.imageBox.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.preview, null))
                binding.edtSubjectName.text = null
                Toast.makeText(requireContext(), "Subject Added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
    }


}