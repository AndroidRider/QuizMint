package com.androidrider.quizmint_admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidrider.quizmint_admin.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private val db = FirebaseFirestore.getInstance()
//    private var selectedSubject: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)










//        binding.buttonSelectSubject.setOnClickListener {
//            val intent = Intent(this, SubjectActivity::class.java)
//            startActivityForResult(intent, REQUEST_CODE_SELECT_SUBJECT)
//        }
//
//
//        binding.buttonSubmit.setOnClickListener {
//            val questionText = binding.edtQuestion.text.toString().trim()
//            val option1 = binding.edtOption1.text.toString().trim()
//            val option2 = binding.edtOption2.text.toString().trim()
//            val option3 = binding.edtOption3.text.toString().trim()
//            val option4 = binding.edtOption4.text.toString().trim()
//            val correctOptionIndex = getCorrectOptionIndex() // Replace with actual logic
//
//            // Validate input
//            if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || correctOptionIndex == -1) {
//                Toast.makeText(this, "Please fill all fields and select the correct option", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val question = Question(questionText, listOf(option1, option2, option3, option4), correctOptionIndex)
//
//            sendQuestionToFirestore(question)
//        }
    }

//    private fun sendQuestionToFirestore(question: Question) {
//        db.collection("Questions")
//            .add(question)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "Question added with ID: ${documentReference.id}")
//                Toast.makeText(this, "Question added successfully", Toast.LENGTH_SHORT).show()
//                clearFields()// Optional: Clear fields or navigate to another screen
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding question", e)
//                Toast.makeText(this, "Failed to add question", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun clearFields() {
//        binding.edtQuestion.text.clear()
//        binding.edtOption1.text.clear()
//        binding.edtOption2.text.clear()
//        binding.edtOption3.text.clear()
//        binding.edtOption4.text.clear()
//        binding.radioGroupOptions.clearCheck()
//    }
//
//    private fun getCorrectOptionIndex(): Int {
//        return when {
//            binding.radioOption1.isChecked -> 1
//            binding.radioOption2.isChecked -> 2
//            binding.radioOption3.isChecked -> 3
//            binding.radioOption4.isChecked -> 4
//            else -> -1
//        }
//    }
//
//    companion object {
//        private const val TAG = "MainActivity"
//    }
}

//data class Question(
//    val questionText: String = "",
//    val options: List<String> = emptyList(),
//    val correctOptionIndex: Int = -1
//)
