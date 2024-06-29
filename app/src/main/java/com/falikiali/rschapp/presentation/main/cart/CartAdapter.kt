package com.falikiali.rschapp.presentation.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.falikiali.rschapp.R
import com.falikiali.rschapp.databinding.ItemCartBinding
import com.falikiali.rschapp.domain.model.ProductCart
import com.falikiali.rschapp.helper.GeneralHelper

class CartAdapter(private val listener: CartListener): ListAdapter<ProductCart, CartAdapter.ViewHolder>(DIFF_CALLBACK) {

    interface CartListener {
        fun onBtnCheckItemClick(productCart: ProductCart, isChecked: Boolean)
        fun onBtnIncreaseItemClick(changeProductCart: Map<String, Int>)
        fun onBtnDecreaseItemClick(changeProductCart: Map<String, Int>)
        fun onBtnRemoveItemClick(productCart: ProductCart)
    }

    private var isLoading: Boolean = true
    private var changedProductCart = mutableMapOf<String, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setAdapterLoadingState(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    fun setChangedProductCart(changeProductCart: Map<String, Int>) {
        this.changedProductCart = changeProductCart.toMutableMap()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductCart) {
            with(binding) {
                btnSelect.isChecked = data.isSelected
                tvProductName.text = data.productName
                tvProductStock.text = "Stock ${data.productStock}"
                tvProductSize.text = "Size ${data.productSize}"
                tvPrice.text = GeneralHelper.convertIntToRupiah(data.totalPrice)
                edQuantity.setText(data.quantity.toString())

                if (changedProductCart.containsKey(data.id)) {
                    changedProductCart[data.id]?.let {
                        edQuantity.setText(it.toString())
                        tvPrice.text = GeneralHelper.convertIntToRupiah((data.totalPrice / data.quantity) * edQuantity.text.toString().toInt())
                    }
                }

                if (data.isSelected) {
                    btnSelect.setIconResource(R.drawable.icon_checked)
                } else {
                    btnSelect.setIconResource(R.drawable.icon_unchecked)
                }

                Glide.with(itemView.context)
                    .load(data.productImage)
                    .into(binding.ivThumbnail)

                btnQuantityDecrease.isVisible = edQuantity.text.toString().toInt() > 1
                btnRemove.isVisible = edQuantity.text.toString().toInt() == 1

                btnSelect.isClickable = !isLoading
                btnRemove.isClickable = !isLoading
                btnQuantityDecrease.isClickable = !isLoading
                btnQuantityIncrease.isClickable = !isLoading

                if (isLoading) {
                    btnQuantityDecrease.setOnClickListener(null)
                    btnQuantityIncrease.setOnClickListener(null)
                    btnRemove.setOnClickListener(null)
                    btnSelect.setOnClickListener(null)
                } else {
                    setBtnQuantityDecreaseClickListener(data)
                    setBtnQuantityIncreaseClickListener(data)
                    setBtnRemoveClickListener(data)
                    setBtnCheckClickListener(data)
                }
            }
        }

        private fun setBtnQuantityIncreaseClickListener(data: ProductCart) {
            with(binding) {
                btnQuantityIncrease.setOnClickListener {
                    if (edQuantity.text.toString().toInt() < data.productStock) {
                        edQuantity.setText((edQuantity.text.toString().toInt() + 1).toString())
                        tvPrice.text = GeneralHelper.convertIntToRupiah((data.totalPrice / data.quantity) * edQuantity.text.toString().toInt())

                        btnQuantityDecrease.isVisible = edQuantity.text.toString().toInt() > 1
                        btnRemove.isVisible = edQuantity.text.toString().toInt() == 1

                        if (edQuantity.text.toString().toInt() != data.quantity) {
                            changedProductCart[data.id] = edQuantity.text.toString().toInt()
                        } else {
                            changedProductCart.remove(data.id)
                        }

                        listener.onBtnIncreaseItemClick(changedProductCart)
                    }
                }
            }
        }

        private fun setBtnQuantityDecreaseClickListener(data: ProductCart) {
            with(binding) {
                btnQuantityDecrease.setOnClickListener {
                    edQuantity.setText((edQuantity.text.toString().toInt() - 1).toString())
                    tvPrice.text = GeneralHelper.convertIntToRupiah((data.totalPrice / data.quantity) * edQuantity.text.toString().toInt())

                    btnQuantityDecrease.isVisible = edQuantity.text.toString().toInt() > 1
                    btnRemove.isVisible = edQuantity.text.toString().toInt() == 1

                    if (edQuantity.text.toString().toInt() != data.quantity) {
                        changedProductCart[data.id] = edQuantity.text.toString().toInt()
                    } else {
                        changedProductCart.remove(data.id)
                    }

                    listener.onBtnDecreaseItemClick(changedProductCart)
                }
            }
        }

        private fun setBtnRemoveClickListener(data: ProductCart) {
            with(binding) {
                btnRemove.setOnClickListener {
                    listener.onBtnRemoveItemClick(data)
                }
            }
        }

        private fun setBtnCheckClickListener(data: ProductCart) {
            with(binding) {
                btnSelect.setOnClickListener {
                    listener.onBtnCheckItemClick(data, !data.isSelected)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductCart>() {
            override fun areItemsTheSame(oldItem: ProductCart, newItem: ProductCart): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductCart, newItem: ProductCart): Boolean {
                return oldItem == newItem
            }

        }
    }

}