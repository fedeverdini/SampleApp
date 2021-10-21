package com.example.sampleapp.db

import androidx.room.*
import com.example.sampleapp.model.rate.RateListResponse

@Dao
interface RateListDao {
    @Query("SELECT * FROM Rates ORDER BY lastUpdate DESC LIMIT 1")
    fun getRateList(): RateListResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: RateListResponse)

    @Delete
    fun delete(user: RateListResponse)

    @Query("DELETE FROM Rates")
    fun deleteAll()
}