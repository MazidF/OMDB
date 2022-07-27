package com.example.omdb.data.local.dp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import com.example.omdb.data.local.dp.IDao
import com.example.omdb.data.model.entity.Genre

@Dao
interface GenreDao : IDao<Genre> {

    @Insert(onConflict = IGNORE)
    override fun insert(vararg items: Genre): List<Long>
}