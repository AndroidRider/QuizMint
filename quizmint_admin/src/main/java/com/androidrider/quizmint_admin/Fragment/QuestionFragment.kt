package com.androidrider.quizmint_admin.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidrider.quizmint_admin.Model.Question
import com.androidrider.quizmint_admin.R
import com.androidrider.quizmint_admin.databinding.FragmentQuestionBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore

class QuestionFragment : Fragment() {

    private lateinit var binding: FragmentQuestionBinding
    private val db = FirebaseFirestore.getInstance()
    private val subjects = mutableListOf<String>()
    private var selectedSubject: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)

        // Access the toolbar view - Show/Hide
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.visibility = View.GONE

        // Setup spinner and load subjects
        setupSubjectSpinner()

        binding.buttonSubmit.setOnClickListener {
            val questionText = binding.edtQuestion.text.toString().trim()
            val option1 = binding.edtOption1.text.toString().trim()
            val option2 = binding.edtOption2.text.toString().trim()
            val option3 = binding.edtOption3.text.toString().trim()
            val option4 = binding.edtOption4.text.toString().trim()
            val correctOptionIndex = getCorrectOptionIndex()

            if (selectedSubject == null) {
                Toast.makeText(activity, "Please select a subject", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || correctOptionIndex == -1) {
                Toast.makeText(activity, "Please fill all fields and select the correct option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val question = Question(questionText, listOf(option1, option2, option3, option4), correctOptionIndex)
            sendQuestionToFirestore(selectedSubject!!, question)
        }

        return binding.root
    }

    private fun setupSubjectSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSubjects.adapter = adapter

        // Load subjects from Firestore
        loadSubjects(adapter)

        // Set listener for spinner item selection
        binding.spinnerSubjects.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSubject = subjects[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSubject = null
            }
        }
    }

    private fun loadSubjects(adapter: ArrayAdapter<String>) {
        db.collection("Subject").get()
            .addOnSuccessListener { documents ->
                subjects.clear()
                for (document in documents) {
                    val subjectName = document.getString("subject")
                    subjectName?.let {
                        subjects.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error loading subjects", e)
                Toast.makeText(activity, "Failed to load subjects", Toast.LENGTH_SHORT).show()
            }
    }


    private fun sendQuestionToFirestore(subject: String, question: Question) {
        // Get a reference to the "Questions" collection
        val questionsCollection = db.collection("Questions")

        // Create a map with question data including subject name
        val questionData = hashMapOf(
            "subjectName" to subject,  // Include the subject name
            "questionText" to question.questionText,
            "options" to question.options,
            "correctOptionIndex" to question.correctOptionIndex
        )

        // Add the question to the "Questions" collection with a unique ID
        questionsCollection.add(questionData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Question added with ID: ${documentReference.id}")
                Toast.makeText(activity, "Question added successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding question", e)
                Toast.makeText(activity, "Failed to add question", Toast.LENGTH_SHORT).show()
            }
    }




    private fun clearFields() {
        binding.edtQuestion.text.clear()
        binding.edtOption1.text.clear()
        binding.edtOption2.text.clear()
        binding.edtOption3.text.clear()
        binding.edtOption4.text.clear()
        binding.radioGroupOptions.clearCheck()
        binding.spinnerSubjects.setSelection(0)
    }

    private fun getCorrectOptionIndex(): Int {
        return when {
            binding.radioOption1.isChecked -> 0
            binding.radioOption2.isChecked -> 1
            binding.radioOption3.isChecked -> 2
            binding.radioOption4.isChecked -> 3
            else -> -1
        }
    }

    companion object {
        private const val TAG = "QuestionFragment"
    }
}
