package com.duyguabbasoglu.hw2.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TupperDao {
    @Insert
    suspend fun insertTupper(tupper: Tupper): Long

    @Delete
    suspend fun deleteTupper(tupper: Tupper)

    @Update
    suspend fun updateTupper(tupper: Tupper)

    @Query("SELECT * FROM tuppers ORDER BY creationDate DESC")
    fun getAllTuppers(): LiveData<List<Tupper>>

    @Insert
    suspend fun insertItem(item: TupperItem)

    @Delete
    suspend fun deleteItem(item: TupperItem)

    @Query("SELECT * FROM tupper_items WHERE tupperOwnerId = :tupperId")
    fun getItemsForTupper(tupperId: Int): LiveData<List<TupperItem>>
}