package mk.grabit.gpay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import mk.grabit.gpay.util.Helper

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey
    @Json(name = "payment_id") var paymentId: String,
    @Json(name = "merchant") val merchant: Merchant,
    @Json(name = "details") val cart: List<Cart>?,
    var timestamp: String = Helper.getCurrentTimestamp(),
    @Json(name = "amount") val amount: Float
)