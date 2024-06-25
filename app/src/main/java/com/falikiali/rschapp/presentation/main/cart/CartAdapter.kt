package com.falikiali.rschapp.presentation.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.falikiali.rschapp.databinding.ItemCartBinding
import com.falikiali.rschapp.domain.model.ProductCart
import com.falikiali.rschapp.helper.ConstantData

class CartAdapter: ListAdapter<ProductCart, CartAdapter.ViewHolder>(DIFF_CALLBACK) {

    val mapChangedProduct = HashMap<String, Int>()
    val mapSelectedProduct = HashMap<String, Pair<Int, Int>>()

    var onClickBtnIncrease: ((HashMap<String, Int>) -> Unit)? = null
    var onClickBtnDecrease: ((HashMap<String, Int>) -> Unit)? = null
    var onUpdateSelectedProduct: ((HashMap<String, Pair<Int, Int>>) -> Unit)? = null
    var onClickBtnRemove: ((ProductCart) -> Unit)? = null
    var onClickBtnSelect: ((String, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
        onUpdateSelectedProduct?.invoke(mapSelectedProduct)
    }

    inner class ViewHolder(private val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductCart) {
            with(binding) {
                btnSelect.isChecked = data.isSelected
                tvProductName.text = data.productName
                tvProductStock.text = "Stock ${data.productStock}"
                tvProductSize.text = "Size ${data.productSize}"
                tvPrice.text = ConstantData.convertIntToRupiah(data.totalPrice)
                edQuantity.setText(data.quantity.toString())

                Glide.with(itemView.context)
                    .load(data.productImage)
                    .into(binding.ivThumbnail)

                btnQuantityDecrease.isVisible = edQuantity.text.toString().toInt() > 1
                btnRemove.isVisible = edQuantity.text.toString().toInt() == 1

                if (data.isSelected) {
                    mapSelectedProduct[data.id] = Pair(data.quantity, data.totalPrice / data.quantity)
                } else {
                    mapSelectedProduct.remove(data.id)
                }

                //When Btn Decrease Clicked
                btnQuantityDecrease.setOnClickListener {
                    if (edQuantity.text.toString().toInt() > 1) {
                        edQuantity.setText((edQuantity.text.toString().toInt() - 1).toString())
                        tvPrice.text = ConstantData.convertIntToRupiah((data.totalPrice / data.quantity) * edQuantity.text.toString().toInt())

                        if (edQuantity.text.toString().toInt() != data.quantity) {
                            mapChangedProduct[data.id] = edQuantity.text.toString().toInt()
                        } else {
                            mapChangedProduct.remove(data.id)
                        }

                        if (data.isSelected) {
                            mapSelectedProduct[data.id] = Pair(edQuantity.text.toString().toInt(), data.totalPrice / data.quantity)
                        }

                        btnQuantityDecrease.isVisible = edQuantity.text.toString().toInt() > 1
                        btnRemove.isVisible = edQuantity.text.toString().toInt() == 1

                        onClickBtnDecrease?.invoke(mapChangedProduct)
                        onUpdateSelectedProduct?.invoke(mapSelectedProduct)
                    }
                }

                //When Btn Increase Clicked
                btnQuantityIncrease.setOnClickListener {
                    if (edQuantity.text.toString().toInt() < data.productStock) {
                        edQuantity.setText((edQuantity.text.toString().toInt() + 1).toString())
                        tvPrice.text = ConstantData.convertIntToRupiah((data.totalPrice / data.quantity) * edQuantity.text.toString().toInt())

                        if (edQuantity.text.toString().toInt() != data.quantity) {
                            mapChangedProduct[data.id] = edQuantity.text.toString().toInt()
                        } else {
                            mapChangedProduct.remove(data.id)
                        }

                        if (data.isSelected) {
                            mapSelectedProduct[data.id] = Pair(edQuantity.text.toString().toInt(), data.totalPrice / data.quantity)
                        }

                        btnQuantityDecrease.isVisible = edQuantity.text.toString().toInt() > 1
                        btnRemove.isVisible = edQuantity.text.toString().toInt() == 1

                        onClickBtnIncrease?.invoke(mapChangedProduct)
                        onUpdateSelectedProduct?.invoke(mapSelectedProduct)
                    }
                }

                //When Btn Remove Clicked
                btnRemove.setOnClickListener {
                    mapChangedProduct.remove(data.id)
                    mapSelectedProduct.remove(data.id)
                    onClickBtnDecrease?.invoke(mapChangedProduct)
                    onUpdateSelectedProduct?.invoke(mapSelectedProduct)
                    onClickBtnRemove?.invoke(data)
                }

                //When Checkbox Clicked
                btnSelect.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        mapSelectedProduct[data.id] = Pair(edQuantity.text.toString().toInt(), data.totalPrice / data.quantity)
                    } else {
                        mapSelectedProduct.remove(data.id)
                    }

                    onUpdateSelectedProduct?.invoke(mapSelectedProduct)
                    onClickBtnSelect?.invoke(data.id, isChecked)
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