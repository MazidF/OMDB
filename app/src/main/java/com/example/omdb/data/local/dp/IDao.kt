package com.example.omdb.data.local.dp

import androidx.room.*

@Dao
interface IDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // sync with server
    suspend fun insert(vararg items: T): List<Long>

    @Update
    suspend fun update(vararg items: T): Int

    @Delete
    suspend fun delete(vararg items: T): Int
}
