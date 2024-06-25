package com.falikiali.rschapp.presentation.main.dashboard.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.falikiali.rschapp.R
import com.falikiali.rschapp.databinding.ItemCategoryBinding
import com.falikiali.rschapp.domain.model.Category

class StoreCategoryAdapter: ListAdapter<Category, StoreCategoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onClickItem: ((Category) -> Unit)? = null

    private var selectedPosition = 0
    private var lastSelectedPosition = 0

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoreCategoryAdapter.ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreCategoryAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Category) {
            binding.btnCategory.text = data.name

            binding.btnCategory.setOnClickListener {
                onClickItem?.invoke(data)

                lastSelectedPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)
            }

            if (selectedPosition == bindingAdapterPosition) {
                with(binding.btnCategory) {
                    strokeWidth = 0
                    setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.md_theme_primary_highContrast))
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.md_theme_onPrimary_highContrast))
                }
            } else {
                with(binding.btnCategory) {
                    strokeWidth = 3
                    setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.md_theme_background_highContrast))
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.md_theme_onBackground_highContrast))
                }
            }
        }
    }
}