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

    @Query("SELECT * FROM tip_history ORDER BY CASE WHEN :columnName = 'payment' AND :order ='asc' THEN payment END ASC, CASE WHEN :columnName = 'payment' AND :order ='desc' THEN payment END DESC, CASE WHEN :columnName = 'payment_date' AND :order ='asc' THEN payment_date END ASC, CASE WHEN :columnName = 'payment_date' AND :order ='desc' THEN payment_date END DESC")
    suspend fun findAll(columnName: String, order: String): List<TipHistory>

    @Query("DELETE FROM tip_history WHERE id in (:ids)")
    suspend fun delete(ids: List<Int>)
}