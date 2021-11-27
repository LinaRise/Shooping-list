package com.example.android.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.shoppinglist.R
import com.example.android.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    var shopList = listOf<ShopItem>()
        set(value) {
            // compare two lists (old and new one)
            val callBack = ShopListDiffCallback(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callBack)
            //send difference to adapter
            diffResult.dispatchUpdatesTo(this)
            field = value
            // notifyDataSetChanged() no need in this if DiffUtil used
        }
    var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            ENABLED -> R.layout.item_shop_enabled
            DISABLED -> R.layout.item_shop_diasbled
            else -> throw RuntimeException("Unknown view type $viewType")
        }

        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        Log.d("ShopListAdapter", "ShopListAdapter ${count++}")

        val shopItem = shopList[position]
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            //invoke if shopItem != null
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            //invoke if shopItem != null
            onShopItemClickListener?.invoke(shopItem)
            true
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

    override fun onViewRecycled(holder: ShopItemViewHolder) {
        super.onViewRecycled(holder)
        holder.tvName.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                android.R.color.white
            )
        )
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopList[position].enabled) {
            ENABLED
        } else {
            DISABLED
        }
    }

    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }

    companion object {
        const val ENABLED = 111
        const val DISABLED = 100
        const val MAX_POOL_SIZE = 15
    }
}