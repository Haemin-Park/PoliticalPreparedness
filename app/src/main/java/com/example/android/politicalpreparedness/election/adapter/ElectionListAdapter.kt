package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(election, clickListener)
    }

    class ElectionViewHolder constructor(private val binding: ItemElectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(election: Election, listener: ElectionListener) {
            binding.run {
                setVariable(BR.election, election)
                setVariable(BR.clickListener, listener)
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ElectionViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemElectionBinding.inflate(inflater, parent, false)
                return ElectionViewHolder(binding)
            }
        }
    }
}

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean =
        oldItem.id == newItem.id
}

class ElectionListener(val clickListener: (election: Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}