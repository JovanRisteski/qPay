package mk.grabit.gpay.data.model

import mk.grabit.gpay.R
import mk.grabit.gpay.util.CreditCardType

data class CreditCard(
    val id: Int?,
    val image: Int = R.drawable.mastercard_logo,
    val expirationDate: String? = "01/21",
    val cardHolder: String? = "John Doe",
    val manufacturer: String? = "Mastercard",
    val lastDigits: String = "1234",
    val type: String? = CreditCardType.CREDIT
)