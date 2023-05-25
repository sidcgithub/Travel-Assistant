package com.siddharthchordia.travelassistant.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.siddharthchordia.travelassistant.R
import com.siddharthchordia.travelassistant.databinding.AttractionItemBinding
import com.siddharthchordia.travelassistant.model.Attraction

class AttractionsAdapter(private val onItemClick: (Attraction) -> Unit) : ListAdapter<Attraction, AttractionsAdapter.AttractionViewHolder>(AttractionDiffCallback()) {

    class AttractionViewHolder(private val binding: AttractionItemBinding, private val onItemClick: (Attraction) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {

                binding.root.setOnClickListener {
                    val attraction = binding.attraction
                    if (attraction != null) {
                        onItemClick(attraction)
                    }
                }

        }
        fun bind(attraction: Attraction) {
            binding.attraction = attraction
            binding.executePendingBindings()
        }
    }

    class AttractionDiffCallback : DiffUtil.ItemCallback<Attraction>() {
        override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem.xid == newItem.xid
        }

        override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AttractionItemBinding.inflate(layoutInflater, parent, false)
        return AttractionViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        val attraction = getItem(position)
        holder.bind(attraction)
    }
}
