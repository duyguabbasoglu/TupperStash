package com.duyguabbasoglu.hw2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyguabbasoglu.hw2.adapter.TupperDetailAdapter
import com.duyguabbasoglu.hw2.databinding.ActivityTupperDetailBinding
import com.duyguabbasoglu.hw2.model.AppDatabase
import com.duyguabbasoglu.hw2.model.Tupper
import com.duyguabbasoglu.hw2.model.TupperItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TupperDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTupperDetailBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: TupperDetailAdapter
    private var currentTupper: Tupper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTupperDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        db = AppDatabase.getDatabase(this)
        currentTupper = intent.getParcelableExtra("selected_tupper")

        if (currentTupper == null) {
            Toast.makeText(this, getString(R.string.error_tupper_not_found), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.tvTupperTitle.text = currentTupper?.name

        setupRecyclerView()

        currentTupper?.let { tupper ->
            db.tupperDao().getItemsForTupper(tupper.uid).observe(this) { items ->
                adapter.setData(items)
            }
        }

        binding.fabAddItem.setOnClickListener {
            showAddItemDialog()
        }

        binding.tvTupperTitle.setOnClickListener {
            showUpdateDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = TupperDetailAdapter { item ->
            lifecycleScope.launch(Dispatchers.IO) {
                db.tupperDao().deleteItem(item)
            }
            Toast.makeText(this, getString(R.string.msg_item_deleted), Toast.LENGTH_SHORT).show()
        }

        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.adapter = adapter
    }

    private fun showAddItemDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val etContent = dialogView.findViewById<EditText>(R.id.etContent)
        val rgType = dialogView.findViewById<RadioGroup>(R.id.rgType)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnAddItem)

        btnAdd.setOnClickListener {
            val content = etContent.text.toString().trim()
            if (content.isEmpty()) {
                etContent.error = getString(R.string.error_empty_content)
                return@setOnClickListener
            }

            val type = if (rgType.checkedRadioButtonId == R.id.rbText) 0 else 1

            val newItem = TupperItem(
                tupperOwnerId = currentTupper!!.uid,
                itemType = type,
                title = "Item",
                contentData = content
            )

            lifecycleScope.launch(Dispatchers.IO) {
                db.tupperDao().insertItem(newItem)
            }
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun showUpdateDialog() {
        val editText = EditText(this)
        editText.setText(currentTupper?.name)

        AlertDialog.Builder(this)
            .setTitle("Rename Tupper")
            .setView(editText)
            .setPositiveButton("Update") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty() && currentTupper != null) {
                    val updatedTupper = currentTupper!!.copy(name = newName)

                    lifecycleScope.launch(Dispatchers.IO) {
                        db.tupperDao().updateTupper(updatedTupper)
                    }

                    binding.tvTupperTitle.text = newName
                    currentTupper = updatedTupper
                    Toast.makeText(this, "Name updated!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}