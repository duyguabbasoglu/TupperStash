package com.duyguabbasoglu.hw2.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "tupper_items",
    foreignKeys = [ForeignKey(
        entity = Tupper::class,
        parentColumns = ["uid"],
        childColumns = ["tupperOwnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TupperItem(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,

    val tupperOwnerId: Int,
    val itemType: Int,
    val title: String,
    val contentData: String
) : Parcelable