package com.androidrider.quizmint.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidrider.quizmint.Model.HistoryModel
import com.androidrider.quizmint.Model.UserModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.FragmentSpinBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class SpinFragment : Fragment() {

    private lateinit var binding: FragmentSpinBinding
    var currentChance=0L
    var currentCoin=0L

//    private lateinit var timer: CountDownTimer
    private var timer: CountDownTimer? = null // CHANGED: Initialize timer as nullable

//    private val itemTitles = arrayOf("100", "Try Again", "500", "Try Again", "200", "Try Again")// NeatRoots Array

/* Write Array reverse order according to wheel and also coin position should be even */
//    private val itemTitles = arrayOf("100", "Try Again", "200", "Try Again", "500", "Try Again") // 6 segment

    private val itemTitles = arrayOf("100", "Try Again", "500", "Try Again", "200", "Try Again", "300", "Try Again") // 8 segment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        binding = FragmentSpinBinding.inflate(inflater, container,false)
        // Inflate the layout for this fragment


        openBottomSheet()//Calling Function
        getDataFromFirebase()//Calling Function
        showCoin()//Calling Function
        showSpinChance()//Calling Function

        navigateToProfileFragment()

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
                        binding.btnSpin.isEnabled = false
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
                        currentCoin=snapshot.getValue() as Long
                        binding.textCoinWithdrawal.text=currentCoin.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
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

    fun showResult(itemTitles: String, spin:Int){

        if (spin%2==0){
            var winCoin=itemTitles.toInt()
            Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
                .setValue(winCoin+currentCoin)

            var historyModel= HistoryModel(winCoin.toString(),System.currentTimeMillis().toString() ,false)
            Firebase.database.reference.child("PlayerCoinHistory").child(Firebase.auth.currentUser!!.uid)
                .push().setValue(historyModel)

            binding.textCoinWithdrawal.text= (winCoin+currentCoin).toString()

        }
        Toast.makeText(requireContext(), itemTitles, Toast.LENGTH_SHORT).show()

        currentChance = currentChance-1
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance)
        binding.btnSpin.isEnabled = true
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







    /* For the 8 Segment */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSpin.setOnClickListener {
            binding.btnSpin.isEnabled = false

            if (currentChance>0){
                val spin = Random.nextInt(8) // Adjusted for 8 items
                val degrees = 45f * spin // Adjusted for 8 items (360 / 8 = 45 degrees per segment)


                timer?.cancel() // CHANGED: Cancel any existing timer before starting a new one
                timer = object : CountDownTimer(5000, 50) // Assign the new timer to the timer variable
                {
                    var rotation = 0f

                    override fun onTick(millisUntilFinished: Long) {
                        rotation += 5f
                        if (rotation >= degrees) {
                            rotation = degrees
//                            timer.cancel()
                            cancel() // CHANGED: Ensure the timer is cancelled to prevent further ticks

                            showResult(itemTitles[spin],spin)
                        }
                        binding.rotationWheel.rotation = rotation
                    }

                    override fun onFinish() {}
                }.start()

            }else{
                Toast.makeText(requireContext(), "Out of Chances", Toast.LENGTH_SHORT).show()
                binding.btnSpin.isEnabled = true // CHANGED: Ensure the button is re-enabled if out of chances
            }

        }
    }




/* For the 6 Segment */
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.btnSpin.setOnClickListener {
//            binding.btnSpin.isEnabled = false
//
//            if (currentChance>0){
//                val spin = Random.nextInt(6) // Adjusted for 8 items
//                val degrees = 60f * spin // Adjusted for 8 items (360 / 8 = 45 degrees per segment)
//
////                timer = object : CountDownTimer(5000, 50)
//                timer?.cancel() // CHANGED: Cancel any existing timer before starting a new one
//                timer = object : CountDownTimer(5000, 50) // Assign the new timer to the timer variable
//                {
//                    var rotation = 0f
//
//                    override fun onTick(millisUntilFinished: Long) {
//                        rotation += 5f
//                        if (rotation >= degrees) {
//                            rotation = degrees
////                            timer.cancel()
//                            cancel() // CHANGED: Ensure the timer is cancelled to prevent further ticks
//
//                            showResult(itemTitles[spin],spin)
//                        }
//                        binding.rotationWheel.rotation = rotation
//                    }
//
//                    override fun onFinish() {}
//                }.start()
//
//            }else{
//                Toast.makeText(requireContext(), "Out of Chances", Toast.LENGTH_SHORT).show()
//                binding.btnSpin.isEnabled = true // CHANGED: Ensure the button is re-enabled if out of chances
//            }
//
//        }
//    }




}




















/* Original code for 6 item*/
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.btnSpin.setOnClickListener {
//            binding.btnSpin.isEnabled = false
//
//            val spin = Random.nextInt(6)
//            val degrees = 60f*spin
//
//            timer = object : CountDownTimer(5000,50){
//                var rotation = 0f
//
//                override fun onTick(millisUntilFinished: Long) {
//                    rotation += 5f
//                    if (rotation >= degrees){
//                        rotation = degrees
//                        timer.cancel()
//                        showResult(itemTitles[spin])
//                    }
//                    binding.rotationWheel.rotation = rotation
//                }
//
//                override fun onFinish() {}
//            }.start()
//        }
//    }
