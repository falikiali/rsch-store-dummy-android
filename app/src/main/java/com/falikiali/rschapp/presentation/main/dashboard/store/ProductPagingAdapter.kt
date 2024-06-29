package com.falikiali.rschapp.presentation.main.dashboard.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.falikiali.rschapp.databinding.ItemProductBinding
import com.falikiali.rschapp.domain.model.Product
import com.falikiali.rschapp.helper.ConstantData
import com.falikiali.rschapp.helper.GeneralHelper

class ProductPagingAdapter: PagingDataAdapter<Product, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    var onClickItem: ((Product) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        data?.let {
            when (holder) {
                is GridViewHolder -> holder.bind(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    inner class GridViewHolder(private val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Product) {
            with(binding) {
                val priceInRupiah = GeneralHelper.convertIntToRupiah(data.price)

                tvName.text = data.name
                tvPrice.text = priceInRupiah
                tvPurpose.text = data.purpose
                tvStock.text = "${data.stock} left"

                Glide.with(itemView.context)
                    .load(data.image)
                    .into(binding.ivThumbnail)
            }

            binding.root.setOnClickListener {
                onClickItem?.invoke(data)
            }
        }
    }

}