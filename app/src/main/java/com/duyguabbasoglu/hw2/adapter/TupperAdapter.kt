package com.duyguabbasoglu.hw2.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyguabbasoglu.hw2.databinding.ItemTupperBinding
import com.duyguabbasoglu.hw2.model.Tupper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TupperAdapter(
    private val onTupperClick: (Tupper) -> Unit,
    private val onDeleteClick: (Tupper) -> Unit
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

            // Tarihi formatla (Long -> String)
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            tvDate.text = sdf.format(Date(currentTupper.creationDate))

            // Renk kodunu uygula (Try-catch hatayı önler)
            try {
                viewColorIndicator.setBackgroundColor(Color.parseColor(currentTupper.colorCode))
            } catch (e: Exception) {
                viewColorIndicator.setBackgroundColor(Color.GRAY) // Hatalı renk gelirse gri yap
            }

            // Tıklama olayları
            root.setOnClickListener { onTupperClick(currentTupper) }
            btnDelete.setOnClickListener { onDeleteClick(currentTupper) }
        }
    }

    override fun getItemCount() = tupperList.size

    // Veriyi güncellemek için bu metodu çağıracağız
    fun setData(newTuppers: List<Tupper>) {
        this.tupperList = newTuppers
        notifyDataSetChanged()
    }
}