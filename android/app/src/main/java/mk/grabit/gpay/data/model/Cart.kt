package mk.grabit.gpay.data.model

import com.squareup.moshi.Json

data class Cart(
    @Json(name = "item_name") val name: String? = "",
    @Json(name = "item_description") val description: String? = "",
    @Json(name = "item_quantity") val quantity: Int? = 1,
    @Json(name = "item_price_mkd") val price: Float? = 0.0f
)