package com.siddharthchordia.travelassistant.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.siddharthchordia.travelassistant.R
import com.siddharthchordia.travelassistant.databinding.FragmentAttractionDetailsBinding
import com.siddharthchordia.travelassistant.databinding.FragmentAttractionsBinding
import com.siddharthchordia.travelassistant.ui.home.adapter.AttractionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttractionDetailsFragment : Fragment() {
    private var _binding: FragmentAttractionDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AttractionDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttractionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: AttractionDetailsFragmentArgs by navArgs()
        val xid = args.xid
        binding.lifecycleOwner = this
        binding.attractionDetails = viewModel
        viewModel.fetchAttractionDetails(xid)
        viewModel.attractionsAdded.observe(this.viewLifecycleOwner) {
            binding.buttonSaveAttraction.visibility = if(viewModel.enableSaveButton(xid, it).not()) {View.INVISIBLE} else { View.VISIBLE}
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}