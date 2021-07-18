package com.coroutinemvi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coroutinemvi.databinding.HolderRecyclerListBinding

class RecyclerListAdapter : RecyclerView.Adapter<RecyclerListHolder>() {
    var data = emptyList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecyclerListHolder(HolderRecyclerListBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerListHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}

class RecyclerListHolder(val binding: HolderRecyclerListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(value: String) {
        binding.root.text = value
    }
}