package com.example.tipjar.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.tipjar.database.base.BaseRoomDao
import com.example.tipjar.database.entities.TipHistory

@Dao
interface TipHistoryDao : BaseRoomDao<TipHistory> {

    @Query("SELECT * FROM tip_history")
    suspend fun findAll(): List<TipHistory>

    @Query("SELECT * FROM tip_history WHERE id = :id")
    suspend fun find(id: Int): TipHistory?

    @Query("SELECT * FROM tip_history ORDER BY payment_date DESC")
    suspend fun findAllSortByDate(): List<TipHistory>

    @Query("DELETE FROM tip_history WHERE id in (:ids)")
    suspend fun delete(ids: List<Int>)
}