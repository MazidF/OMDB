package com.example.omdb.data.local.dp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface IDao<T> {
    @Insert
    fun insert(vararg items: T): List<Long>

    @Update
    fun update(vararg items: T): Int

    @Delete
    fun delete(vararg items: T): Int
}
