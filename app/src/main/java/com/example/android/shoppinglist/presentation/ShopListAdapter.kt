package com.example.android.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.android.shoppinglist.R
import com.example.android.shoppinglist.databinding.ItemShopDiasbledBinding
import com.example.android.shoppinglist.databinding.ItemShopEnabledBinding
import com.example.android.shoppinglist.domain.ShopItem

class ShopListAdapter :
    ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {
//class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {  used when used DiffUtil on list comparison

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    /*var shopList = listOf<ShopItem>() // romve when DiffUtil.ItemCallback used
        set(value) {
            // compare two lists (old and new one)
            val callBack = ShopListDiffCallback(shopList, value)
            // calculateDiff - performs in main thread so it could affect performance on weak devices
            val diffResult = DiffUtil.calculateDiff(callBack)
            //send difference to adapter
            diffResult.dispatchUpdatesTo(this)
            field = value
            // notifyDataSetChanged() no need in this if DiffUtil used
        }*/
    var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            ENABLED -> R.layout.item_shop_enabled
            DISABLED -> R.layout.item_shop_diasbled
            else -> throw RuntimeException("Unknown view type $viewType")
        }

        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layout,
                parent,
                false
            )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        Log.d("ShopListAdapter", "ShopListAdapter ${count++}")

//        val shopItem = shopList[position]
        val binding = holder.binding
        val shopItem = getItem(position)
        when (binding) {
            is ItemShopDiasbledBinding -> {
                binding.shopItem = shopItem
            }

            is ItemShopEnabledBinding -> {
                binding.shopItem = shopItem
            }
        }
        binding.root.setOnLongClickListener {
            //invoke if shopItem != null
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener {
            //invoke if shopItem != null
            onShopItemClickListener?.invoke(shopItem)
        }
        /*if (shopItem.enabled) {
            holder.tvName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    android.R.color.holo_orange_light
                )
            )
        }
        //block else does not to be used because of the onViewRecycled() method implementation
         else{
             holder.tvName.setTextColor(ContextCompat.getColor(holder.itemView.context,android.R.color.white))
         }*/
    }

    /*   override fun getItemCount(): Int { remove wheb DiffUtil.ItemCallback and ListAdapter used cause ListAdapter already implements it
           return shopList.size
       }*/

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) {
            ENABLED
        } else {
            DISABLED
        }
    }

    companion object {
        const val ENABLED = 111
        const val DISABLED = 100
        const val MAX_POOL_SIZE = 15
    }
}