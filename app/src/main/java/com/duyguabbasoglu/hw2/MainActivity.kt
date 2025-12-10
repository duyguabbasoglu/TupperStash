package com.duyguabbasoglu.hw2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyguabbasoglu.hw2.adapter.TupperAdapter
import com.duyguabbasoglu.hw2.databinding.ActivityMainBinding
import com.duyguabbasoglu.hw2.model.AppDatabase
import com.duyguabbasoglu.hw2.model.Tupper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

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
    }

    private fun setupRecyclerView() {
        adapter = TupperAdapter(
            onTupperClick = { tupper -> onTupperClicked(tupper) },
            onDeleteClick = { tupper -> deleteTupperFromDb(tupper) }
        )
        binding.rvTupperList.layoutManager = LinearLayoutManager(this)
        binding.rvTupperList.adapter = adapter
    }

    private fun deleteTupperFromDb(tupper: Tupper) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.tupperDao().deleteTupper(tupper)
        }
        Toast.makeText(this, "${tupper.name} ${getString(R.string.msg_tupper_deleted)}", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, getString(R.string.msg_tupper_created), Toast.LENGTH_SHORT).show()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun onTupperClicked(tupper: Tupper) {
        val intent = Intent(this, TupperDetailActivity::class.java)
        intent.putExtra("selected_tupper", tupper)
        startActivity(intent)
    }
}