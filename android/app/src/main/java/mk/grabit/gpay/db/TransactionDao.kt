package mk.grabit.gpay.db

import androidx.lifecycle.LiveData
import androidx.room.*
import mk.grabit.gpay.data.model.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<Transaction>)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction` ORDER BY paymentId DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM `transaction` ORDER BY paymentId DESC LIMIT 5")
    fun getLastTransactions(): LiveData<List<Transaction>>
}