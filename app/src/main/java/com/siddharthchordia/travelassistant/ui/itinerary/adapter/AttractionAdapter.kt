package com.siddharthchordia.travelassistant.ui.itinerary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.siddharthchordia.travelassistant.databinding.ListItemItineraryBinding
import com.siddharthchordia.travelassistant.model.Attraction
import com.siddharthchordia.travelassistant.model.AttractionEntity

class AttractionsAdapter(private val onItemClick: AttractionListener) : ListAdapter<AttractionEntity, AttractionsAdapter.AttractionViewHolder>(AttractionDiffCallback()) {
    interface AttractionListener {
        fun onDoneClicked(attraction: AttractionEntity)

        fun onRemoveClicked(attraction: AttractionEntity)
    }


    class AttractionViewHolder(private val binding: ListItemItineraryBinding, private val onItemClick: AttractionListener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(attraction: AttractionEntity) {
            binding.attraction = attraction
            binding.btnMarkAsDone.setOnClickListener { onItemClick.onDoneClicked(attraction ) }
            binding.btnRemove.setOnClickListener { onItemClick.onRemoveClicked(attraction ) }
            binding.executePendingBindings()
        }
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
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemItineraryBinding.inflate(layoutInflater, parent, false)
        return AttractionViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        val attraction = getItem(position)
        holder.bind(attraction)
    }
}
