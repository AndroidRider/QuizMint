package com.androidrider.quizmint_admin.Fragment

import android.content.Intent
import com.androidrider.quizmint_admin.databinding.FragmentHomeBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.androidrider.quizmint_admin.Activity.ViewSubjectActivity
import com.androidrider.quizmint_admin.R
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        // Access the toolbar view - Show/Hide
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title="Home"

        binding.button1.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_subjectFragment)
        }

        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addQuestionFragment)
        }

        binding.button3.setOnClickListener {
            startActivity(Intent(requireContext(), ViewSubjectActivity::class.java))
        }

        binding.button4.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userListFragment)
        }


        return binding.root
    }


}