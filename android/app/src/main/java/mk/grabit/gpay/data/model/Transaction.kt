package mk.grabit.gpay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey
    @Json(name = "payment_id") var paymentId: String,
    @Json(name = "merchant") val merchant: Merchant,
    @Json(name = "cart_info") val cart: List<Cart>,
    @Json(name = "amount") val amount: Float
)