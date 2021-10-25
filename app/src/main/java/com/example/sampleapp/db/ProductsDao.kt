package com.example.sampleapp.db

import androidx.room.*
import com.example.sampleapp.ui.transaction.view.ProductView

@Dao
interface ProductsDao {
    @Query("SELECT sku FROM Products LIMIT :pageSize OFFSET :page * :pageSize")
    fun getProductList(page: Int, pageSize: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg product: ProductView)

    @Delete
    fun delete(product: ProductView)

    @Query("DELETE FROM Products")
    fun deleteAll()
}