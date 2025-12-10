package com.duyguabbasoglu.hw2.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyguabbasoglu.hw2.databinding.ItemTupperBinding
import com.duyguabbasoglu.hw2.model.Tupper
import com.duyguabbasoglu.hw2.view.TupperIconView // Bunu import ettiğinden emin ol
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TupperAdapter(
    private val listener: TupperListener
) : RecyclerView.Adapter<TupperAdapter.TupperViewHolder>() {

    private var tupperList = emptyList<Tupper>()

    class TupperViewHolder(val binding: ItemTupperBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TupperViewHolder {
        val binding = ItemTupperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TupperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TupperViewHolder, position: Int) {
        val currentTupper = tupperList[position]

        holder.binding.apply {
            tvTupperName.text = currentTupper.name

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            tvDate.text = sdf.format(Date(currentTupper.creationDate))
            (ivTupperIcon as? TupperIconView)?.setTupperColor(currentTupper.colorCode)

            ivTupperIcon.setOnClickListener {
                android.widget.Toast.makeText(root.context, "Color: ${currentTupper.colorCode}", android.widget.Toast.LENGTH_SHORT).show()
            }

            root.setOnClickListener { listener.onTupperClicked(currentTupper) }
            btnDelete.setOnClickListener { listener.onTupperDeleted(currentTupper) }
        }
    }

    override fun getItemCount() = tupperList.size

    fun setData(newTuppers: List<Tupper>) {
        this.tupperList = newTuppers
        notifyDataSetChanged()
    }
}