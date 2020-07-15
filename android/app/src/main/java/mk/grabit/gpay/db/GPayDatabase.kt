package mk.grabit.gpay.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mk.grabit.gpay.data.model.CartTypeConverter
import mk.grabit.gpay.data.model.MerchantTypeConverter
import mk.grabit.gpay.data.model.Transaction

@Database(
    entities = [Transaction::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(
    CartTypeConverter::class,
    MerchantTypeConverter::class
)
abstract class GPayDatabase : RoomDatabase() {

    abstract fun getTransactionDao(): TransactionDao
}