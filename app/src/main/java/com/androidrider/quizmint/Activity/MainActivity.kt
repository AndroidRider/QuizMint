package com.androidrider.quizmint.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androidrider.quizmint.Fragment.HistoryFragment
import com.androidrider.quizmint.Fragment.ProfileFragment
import com.androidrider.quizmint.Fragment.SpinFragment
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController=findNavController(R.id.fragmentContainerView)
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)




//        fragmentTransactionAndGoingPlayToProfile()

    }


//    this code is written for the going playActivity to profileFragment
private fun fragmentTransactionAndGoingPlayToProfile(){
       binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    // Handle home navigation
                    true
                }
                R.id.spinFragment -> {

                    // Perform the fragment transaction
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SpinFragment())
                        .commit()
                    true
                }
                R.id.historyFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, HistoryFragment())
                        .commit()
                    true
                }
                R.id.profileFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ProfileFragment())
                        .commit()
                    true
                }
                // Add other navigation items here
                else -> false
            }
        }

        // Check if the intent has the extra to open the profile fragment
        if (intent.getBooleanExtra("openProfile", false)) {
            binding.bottomNavigationView.selectedItemId = R.id.profileFragment
        }
    }


}
