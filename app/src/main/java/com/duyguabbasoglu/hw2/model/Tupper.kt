package com.duyguabbasoglu.hw2.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tuppers")
data class Tupper(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,

    val name: String,
    val creationDate: Long,
    val colorCode: String
) : Parcelable