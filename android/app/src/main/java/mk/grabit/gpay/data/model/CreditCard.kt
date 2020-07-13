package mk.grabit.gpay.data.model

import mk.grabit.gpay.util.CreditCardType

data class CreditCard(
    val id: Int?,
    val expirationDate: String? = "01/21",
    val cardHolder: String? = "John Doe",
    val manufacturer: String? = "Mastercard",
    val type: String? = CreditCardType.CREDIT
) {
}