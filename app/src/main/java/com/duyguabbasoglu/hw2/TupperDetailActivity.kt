package com.duyguabbasoglu.hw2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyguabbasoglu.hw2.databinding.ActivityTupperDetailBinding
import com.duyguabbasoglu.hw2.model.AppDatabase
import com.duyguabbasoglu.hw2.model.Tupper

class TupperDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTupperDetailBinding
    private lateinit var db: AppDatabase
    private var currentTupper: Tupper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTupperDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        // Intent ile gelen Tupper verisini al (Parcelable)
        currentTupper = intent.getParcelableExtra("selected_tupper")

        if (currentTupper == null) {
            Toast.makeText(this, "Hata: Kutu bulunamadı!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Başlığı ayarla
        binding.tvTupperTitle.text = currentTupper?.name

        setupRecyclerView()

        // Sadece bu kutuya ait item'ları getir
        currentTupper?.let { tupper ->
            db.tupperDao().getItemsForTupper(tupper.uid).observe(this) { items ->
                // Adapter'a items listesini gönder
                // adapter.submitList(items)
            }
        }

        binding.fabAddItem.setOnClickListener {
            // ÖDEV ŞARTI: Buraya Custom View içeren Dialog veya Activity gelecek
            // showAddItemDialog()
        }
    }

    private fun setupRecyclerView() {
        binding.rvItems.layoutManager = LinearLayoutManager(this)
        // Adapter burada tanımlanacak (Multi-layout adapter)
    }
}