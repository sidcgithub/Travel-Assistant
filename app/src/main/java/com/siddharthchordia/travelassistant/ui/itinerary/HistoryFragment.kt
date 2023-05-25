package com.siddharthchordia.travelassistant.ui.itinerary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.siddharthchordia.travelassistant.R
import com.siddharthchordia.travelassistant.databinding.FragmentHistoryBinding
import com.siddharthchordia.travelassistant.model.AttractionEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHistoryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = SimpleAttractionAdapter()
        binding.attractionsRecyclerView.adapter = adapter

        viewModel.completedAttractions.observe(viewLifecycleOwner) { attractions ->
            adapter.submitList(attractions)
        }

        return binding.root
    }
}

class SimpleAttractionAdapter : ListAdapter<AttractionEntity, SimpleAttractionAdapter.AttractionViewHolder>(AttractionDiffCallback()) {

    inner class AttractionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val attractionName: TextView = itemView.findViewById(R.id.attraction_name)
    }

    class AttractionDiffCallback : DiffUtil.ItemCallback<AttractionEntity>() {
        override fun areItemsTheSame(oldItem: AttractionEntity, newItem: AttractionEntity): Boolean {
            return oldItem.xid == newItem.xid
        }

        override fun areContentsTheSame(oldItem: AttractionEntity, newItem: AttractionEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_attraction_item, parent, false)
        return AttractionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        val attraction = getItem(position)
        holder.attractionName.text = attraction.name
    }
}
