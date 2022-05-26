package com.acedrops.acedrops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.acedrops.acedrops.R
import com.acedrops.acedrops.databinding.AddressItemBinding
import com.acedrops.acedrops.model.AddressResponse

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    var addressList = mutableListOf<AddressResponse>()
    fun updateAddressList(category: List<AddressResponse>) {
        this.addressList = category.toMutableList()
        notifyDataSetChanged()
    }

    private var mlistner: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistner = listener
    }

    class ViewHolder(val binding: AddressItemBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: AddressResponse) {
            binding.address = address
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AddressItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.address_item, parent, false
        )
        return ViewHolder(binding, mlistner!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(addressList[position])
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

}