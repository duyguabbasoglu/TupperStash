package com.duyguabbasoglu.hw2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyguabbasoglu.hw2.adapter.TupperAdapter
import com.duyguabbasoglu.hw2.adapter.TupperListener
import com.duyguabbasoglu.hw2.api.ApiClient
import com.duyguabbasoglu.hw2.api.ApiResponse
import com.duyguabbasoglu.hw2.api.ApiService
import com.duyguabbasoglu.hw2.databinding.ActivityMainBinding
import com.duyguabbasoglu.hw2.model.AppDatabase
import com.duyguabbasoglu.hw2.model.Tupper
import com.duyguabbasoglu.hw2.model.TupperItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), TupperListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: TupperAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        setupRecyclerView()

        db.tupperDao().getAllTuppers().observe(this) { listOfTuppers ->
            adapter.setData(listOfTuppers)
        }

        binding.fabAddTupper.setOnClickListener {
            showAddTupperDialog()
        }

        binding.btnDownloadRecipe.setOnClickListener {
            val service = ApiClient.getClient().create(ApiService::class.java)
            val call = service.getOnlineData()

            Toast.makeText(this, "Downloading custom tuppers...", Toast.LENGTH_SHORT).show()

            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.tupperList != null) {

                        val onlineList = response.body()!!.tupperList!!

                        lifecycleScope.launch(Dispatchers.IO) {
                            for (apiTupper in onlineList) {
                                val newTupper = Tupper(
                                    name = apiTupper.name,
                                    creationDate = System.currentTimeMillis(),
                                    colorCode = apiTupper.color
                                )
                                val tupperId = db.tupperDao().insertTupper(newTupper).toInt()

                                for (apiItem in apiTupper.items) {
                                    val typeCode = if (apiItem.type == "image") 1 else 0
                                    val newItem = TupperItem(
                                        tupperOwnerId = tupperId,
                                        itemType = typeCode,
                                        title = apiItem.title,
                                        contentData = apiItem.content
                                    )
                                    db.tupperDao().insertItem(newItem)
                                }
                            }
                        }

                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "All tuppers imported! 🎉", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Connection Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setupRecyclerView() {
        // Interface (this) gönderiyoruz
        adapter = TupperAdapter(this)
        binding.rvTupperList.layoutManager = LinearLayoutManager(this)
        binding.rvTupperList.adapter = adapter
    }

    // Interface Metodları
    override fun onTupperClicked(tupper: Tupper) {
        val intent = Intent(this, TupperDetailActivity::class.java)
        intent.putExtra("selected_tupper", tupper)
        startActivity(intent)
    }

    override fun onTupperDeleted(tupper: Tupper) {
        deleteTupperFromDb(tupper)
    }

    private fun deleteTupperFromDb(tupper: Tupper) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.tupperDao().deleteTupper(tupper)
        }
        Toast.makeText(this, "${tupper.name} deleted", Toast.LENGTH_SHORT).show()
    }

    private fun showAddTupperDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_add_tupper, null)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogBinding)
            .create()

        val etName = dialogBinding.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etTupperName)
        val spColor = dialogBinding.findViewById<android.widget.Spinner>(R.id.spColor)
        val btnSave = dialogBinding.findViewById<android.widget.Button>(R.id.btnSave)

        val colorsMap = mapOf(
            "Red" to "#F44336",
            "Blue" to "#2196F3",
            "Green" to "#4CAF50",
            "Orange" to "#FF9800",
            "Purple" to "#9C27B0",
            "Pink" to "#E91E63",
            "Teal" to "#009688"
        )

        val spinnerAdapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            colorsMap.keys.toList()
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spColor.adapter = spinnerAdapter

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                etName.error = getString(R.string.error_empty_name)
                return@setOnClickListener
            }

            val selectedColorName = spColor.selectedItem.toString()
            val selectedColorCode = colorsMap[selectedColorName] ?: "#808080"

            val newTupper = Tupper(
                name = name,
                creationDate = System.currentTimeMillis(),
                colorCode = selectedColorCode
            )

            lifecycleScope.launch(Dispatchers.IO) {
                db.tupperDao().insertTupper(newTupper)
            }

            dialog.dismiss()
            Toast.makeText(this, "Tupper Created!", Toast.LENGTH_SHORT).show()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}