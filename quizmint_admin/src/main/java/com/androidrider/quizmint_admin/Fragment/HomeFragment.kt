package com.androidrider.quizmint_admin.Fragment

import com.androidrider.quizmint_admin.databinding.FragmentHomeBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.androidrider.quizmint_admin.R
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        // Access the toolbar view - Show/Hide
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.visibility = View.GONE

        binding.button1.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_subjectFragment)
        }

        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_questionFragment)
        }



        return binding.root
    }


}