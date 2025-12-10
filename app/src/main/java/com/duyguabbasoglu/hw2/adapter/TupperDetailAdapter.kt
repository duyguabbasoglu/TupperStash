package com.duyguabbasoglu.hw2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duyguabbasoglu.hw2.R
import com.duyguabbasoglu.hw2.model.TupperItem
import com.squareup.picasso.Picasso // Picasso import edildi

class TupperDetailAdapter(
    private val onDeleteClick: (TupperItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = emptyList<TupperItem>()

    companion object {
        private const val TYPE_TEXT = 0
        private const val TYPE_IMAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].itemType
    }

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMeme: ImageView = itemView.findViewById(R.id.ivMeme)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_TEXT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_text, parent, false)
            TextViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_image, parent, false)
            ImageViewHolder(view)
        }
    }

    // image upload
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = items[position]

        holder.itemView.setOnLongClickListener {
            onDeleteClick(currentItem)
            true
        }

        if (getItemViewType(position) == TYPE_TEXT) {
            (holder as TextViewHolder).tvContent.text = currentItem.contentData
        } else {
            val imageView = (holder as ImageViewHolder).ivMeme
            val content = currentItem.contentData.trim()

            if (content.startsWith("http")) {
                Picasso.get()
                    .load(content)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(imageView)
            } else {
                val context = holder.itemView.context
                try {
                    val imageResId = context.resources.getIdentifier(
                        content,
                        "drawable",
                        context.packageName
                    )
                    if (imageResId != 0) {
                        imageView.setImageResource(imageResId)
                    } else {
                        imageView.setImageResource(R.mipmap.ic_launcher)
                    }
                } catch (e: Exception) {
                    imageView.setImageResource(R.mipmap.ic_launcher)
                }
            }
        }
    }

    override fun getItemCount() = items.size

    fun setData(newItems: List<TupperItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}