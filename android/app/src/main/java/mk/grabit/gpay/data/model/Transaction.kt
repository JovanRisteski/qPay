package mk.grabit.gpay.data.model

import androidx.room.Entity
import com.squareup.moshi.Json
import mk.grabit.gpay.util.TransactionType

@Entity(tableName = "transaction")
public data class Transaction(
    val id: Int,
    @Json(name = "transaction_id")
    val transactionId: String? = "000000000000",
    val amount: Float? = 0F,
    val recipient: String? = "None",
    val description: String? = "Outcome",
    val type: String? = TransactionType.OUTCOME
) {
    // TODO consider adding local id if API does not return an ID
//    @PrimaryKey(autoGenerate = true)
//    var id: Int? = null
}