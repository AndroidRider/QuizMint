package com.androidrider.quizmint.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidrider.quizmint.Fragment.ProfileFragment
import com.androidrider.quizmint.Fragment.WithdrawalFragment
import com.androidrider.quizmint.Model.QuestionModel
import com.androidrider.quizmint.Model.UserModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.ActivityPlayScreenBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PlayScreen : AppCompatActivity() {

    private lateinit var binding: ActivityPlayScreenBinding
    private lateinit var dialog: Dialog
    private val db = FirebaseFirestore.getInstance()
    private var subjectName: String? = null
    private var questionList = mutableListOf<QuestionModel>()
    private lateinit var currentQuestion: QuestionModel
    private var currentQuestionIndex = 0
    private var currentChance = 0L
    private var qIndex = 0
    private var score = 0
    private var isOptionSelected = false


    companion object {
        const val PASSING_PERCENTAGE = 50 // Define your passing percentage here
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()

        subjectName = intent.getStringExtra("subjectName")
        val image = intent.getStringExtra("subjectImage")
        binding.categoryName.text = subjectName
        Glide.with(this).load(image).into(binding.subjectImage)

        fetchSpinChance()//Calling Function
        openBottomSheet()//Calling Function
        getDataFromFirebase()//Calling Function
        showCoin()//Calling Function
        actionOptionButton()//Calling Function
        fetchQuestionsFromFirestore(subjectName!!)
        handleNextButtonClick()

        navigateToProfileFragment()

    }




    private fun nextQuestionAndScoreUpdate(selectedOption: String, selectedView: View) {

        val currentQuestion = questionList[currentQuestionIndex]
        val correctOptionView: View

        // Identify the correct option view
        when (currentQuestion.correctOptionIndex) {
            0 -> correctOptionView = binding.optionA
            1 -> correctOptionView = binding.optionB
            2 -> correctOptionView = binding.optionC
            3 -> correctOptionView = binding.optionD
            else -> return
        }

        // Check if selectedOption matches the correct option
        if (selectedOption == currentQuestion.options[currentQuestion.correctOptionIndex]) {
            score += 10 // Increment score for correct answer
            selectedView.setBackgroundResource(R.drawable.bg_option_correct) // Correct answer turns green
        } else {
            selectedView.setBackgroundResource(R.drawable.bg_option_incorrect) // Incorrect answer turns red
            correctOptionView.setBackgroundResource(R.drawable.bg_option_correct) // Correct answer turns green
        }

        Log.d(
            "QuizApp",
            "Selected Option: $selectedOption, Correct Option: ${currentQuestion.correctOptionIndex}, Score: $score"
        )
        isOptionSelected = true
    }

    private fun handleNextButtonClick() {
        binding.btnNext.setOnClickListener {

            if (isOptionSelected) {
                // Reset colors
                resetOptionBackgrounds()

                currentQuestionIndex++ // Move to the next question

                // Check if all questions have been answered
                if (currentQuestionIndex >= questionList.size) {
                    val totalPossibleScore = questionList.size * 10
                    val passingScore = (totalPossibleScore * PASSING_PERCENTAGE) / 100

                    if (score >= passingScore) {
                        binding.animWinner.visibility = View.VISIBLE
                        Firebase.database.reference.child("PlayChance")
                            .child(Firebase.auth.currentUser!!.uid)
                            .setValue(currentChance + 1)
                    } else {
                        binding.animSorry.visibility = View.VISIBLE
                    }
                } else {
                    // Display next question
                    val nextQuestion = questionList[currentQuestionIndex]
                    binding.tvQuestion.text = nextQuestion.questionText
                    binding.optionA.text = nextQuestion.options[0]
                    binding.optionB.text = nextQuestion.options[1]
                    binding.optionC.text = nextQuestion.options[2]
                    binding.optionD.text = nextQuestion.options[3]
                }

                isOptionSelected = false // Reset option selected flag
            } else {
                Toast.makeText(this@PlayScreen, "Please select any one option", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun resetOptionBackgrounds() {
        binding.optionA.setBackgroundResource(R.drawable.bg_option_default)
        binding.optionB.setBackgroundResource(R.drawable.bg_option_default)
        binding.optionC.setBackgroundResource(R.drawable.bg_option_default)
        binding.optionD.setBackgroundResource(R.drawable.bg_option_default)
    }

    private fun actionOptionButton() {
        binding.optionA.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionA.text.toString(), binding.optionA)
        }
        binding.optionB.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionB.text.toString(), binding.optionB)
        }
        binding.optionC.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionC.text.toString(), binding.optionC)
        }
        binding.optionD.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionD.text.toString(), binding.optionD)
        }
    }


    private fun fetchQuestionsFromFirestore(subjectName: String) {
        db.collection("Questions")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val question = document.toObject<QuestionModel>()
                        if (question.subjectName == subjectName) {
                            questionList.add(question)
                        }
                    }
                    if (questionList.isEmpty()) {
                        Toast.makeText(
                            this,
                            "No questions available for the subject: $subjectName",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        initViews()
                    }
                }
                dialog.dismiss()
            }
            .addOnFailureListener { e ->
                Log.w("PlayActivity", "Error fetching questions", e)
                Toast.makeText(this, "Failed to fetch questions", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.apply {
            if (questionList.isNotEmpty()) {
                currentQuestion = questionList[qIndex]
                tvQuestion.text = currentQuestion.questionText
                optionA.text = currentQuestion.options[0]
                optionB.text = currentQuestion.options[1]
                optionC.text = currentQuestion.options[2]
                optionD.text = currentQuestion.options[3]

            } else {
                Toast.makeText(this@PlayScreen, "No questions available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openBottomSheet() {
        binding.iconCoinWithdrawal.setOnClickListener {
            val bottomSheetDialog = WithdrawalFragment()
            bottomSheetDialog.show(this.supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        binding.textCoinWithdrawal.setOnClickListener {
            val bottomSheetDialog = WithdrawalFragment()
            bottomSheetDialog.show(this.supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
    }

    private fun fetchSpinChance() {
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        currentChance = snapshot.getValue() as Long
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun showCoin() {
        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var currentCoin = snapshot.getValue() as Long
                        binding.textCoinWithdrawal.text = currentCoin.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun getDataFromFirebase() {
        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var user = snapshot.getValue(UserModel::class.java)

                        binding.tvName.text = user?.name

                        if (user?.profileImageUrl != null) {
                            Glide.with(this@PlayScreen).load(user.profileImageUrl)
                                .into(binding.profileImage)
                        }

                        dialog.dismiss()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // TODO("Not yet implemented")
                    }
                }
            )
    }


    private fun navigateToProfileFragment() {

        binding.profileImage.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openProfile", true)
            startActivity(intent)
            finish()
        }
        binding.tvName.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openProfile", true)
            startActivity(intent)
            finish()
        }

    }

    /* Correct but with auto next question */

//    private fun nextQuestionAndScoreUpdate(selectedOption: String, selectedView: View) {
//        val currentQuestion = questionList[currentQuestionIndex]
//        val correctOptionView: View
//
//        // Identify the correct option view
//        when (currentQuestion.correctOptionIndex) {
//            0 -> correctOptionView = binding.optionA
//            1 -> correctOptionView = binding.optionB
//            2 -> correctOptionView = binding.optionC
//            3 -> correctOptionView = binding.optionD
//            else -> return
//        }
//        // Check if selectedOption matches the correct option
//        if (selectedOption == currentQuestion.options[currentQuestion.correctOptionIndex]) {
//            score += 10 // Increment score for correct answer
//
//            selectedView.setBackgroundResource(R.drawable.bg_option_correct) // Correct answer turns green
//        } else {
//            selectedView.setBackgroundResource(R.drawable.bg_option_incorrect) //incorrect answer turns red
//            correctOptionView.setBackgroundResource(R.drawable.bg_option_correct) // Correct answer turns green
//        }
//
//        Log.d("QuizApp", "Selected Option: $selectedOption, Correct Option: ${currentQuestion.correctOptionIndex}, Score: $score")
//        currentQuestionIndex++ // Move to the next question
//
//        // Delay for showing colors before moving to the next question
//        selectedView.postDelayed({
//            // Reset colors
//            selectedView.setBackgroundResource(R.drawable.shape_button)
//            correctOptionView.setBackgroundResource(R.drawable.shape_button)
//
//            // Check if all questions have been answered
//            if (currentQuestionIndex >= questionList.size) {
//                val totalPossibleScore = questionList.size * 10
//                val passingScore = (totalPossibleScore * PASSING_PERCENTAGE) / 100
//
//                if (score >= passingScore) {
//                    binding.animWinner.visibility = View.VISIBLE
//                    Firebase.database.reference.child("PlayChance")
//                        .child(Firebase.auth.currentUser!!.uid)
//                        .setValue(currentChance + 1)
//                } else {
//                    binding.animSorry.visibility = View.VISIBLE
//                }
//            } else {
//                // Display next question
//                val nextQuestion = questionList[currentQuestionIndex]
//                binding.tvQuestion.text = nextQuestion.questionText
//                binding.optionA.text = nextQuestion.options[0]
//                binding.optionB.text = nextQuestion.options[1]
//                binding.optionC.text = nextQuestion.options[2]
//                binding.optionD.text = nextQuestion.options[3]
//            }
//        }, 2000) // Delay of 2 seconds
//    }


}