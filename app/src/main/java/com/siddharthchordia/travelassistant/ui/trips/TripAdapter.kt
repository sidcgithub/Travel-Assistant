package com.siddharthchordia.travelassistant.ui.trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.siddharthchordia.travelassistant.databinding.ListItemTrpBinding

class TripAdapter : ListAdapter<TripItem, TripAdapter.TripViewHolder>(TripDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        return TripViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class TripViewHolder private constructor(private val binding: ListItemTrpBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TripItem) {
            binding.trip = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TripViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTrpBinding.inflate(layoutInflater, parent, false)
                return TripViewHolder(binding)
            }
        }
    }
}

class TripDiffCallback : DiffUtil.ItemCallback<TripItem>() {
    override fun areItemsTheSame(oldItem: TripItem, newItem: TripItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TripItem, newItem: TripItem): Boolean {
        return oldItem == newItem
    }
}
