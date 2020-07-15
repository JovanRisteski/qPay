package mk.grabit.gpay.util

import mk.grabit.gpay.R
import mk.grabit.gpay.data.model.Cart
import mk.grabit.gpay.data.model.CreditCard
import mk.grabit.gpay.data.model.Merchant
import mk.grabit.gpay.data.model.Transaction

object Data {
    val INITIAL_TRANSACTIONS = arrayListOf(
        Transaction(
            "123",
            Merchant("Tinex", "Shopping"),
            arrayListOf(Cart("kafe", "Paskalin", 12, 70f)),
            1400.00f
        ),
        Transaction(
            "123",
            Merchant("SportVision MK", "Clothes & Shoes"),
            arrayListOf(
                Cart(
                    "T-Shirt",
                    "SportVision collection summer 2020",
                    2,
                    1200f
                )
            ),
            1200.00f
        ),
        Transaction(
            "124",
            Merchant("Burger King", "Food & Drinks"),
            arrayListOf(
                Cart(
                    "Double Burger",
                    "Double burger with cheese, extra sauce",
                    2,
                    170f
                ),
                Cart("CocaCola", "CocaCola, big cup", 2, 140f)
            ),
            480.00f
        ), Transaction(
            "125",
            Merchant("Timberland Center", "Shoes Store"),
            arrayListOf(Cart("Shoes", "Leather Shoes model #482", 1, 3480f)),
            3480.00f
        ), Transaction(
            "126",
            Merchant("Bet365", "Bet & Win"),
            arrayListOf(Cart("Sport ticket", "Bet & Win sport tickets 24/7", 5, 100f)),
            500.00f
        )
        , Transaction(
            "127",
            Merchant("Ramstore Mall Centar", "Shopping"),
            arrayListOf(
                Cart("kafe", "Paskalin", 1, 49f),
                Cart("Illy Coffee", "Coffee Illy ARABICA 500g", 1, 540f),
                Cart("Coffee", "Gold", 1, 70f)
            ),
            659.00f
        )
        , Transaction(
            "128",
            Merchant("GANT", "Clothes"),
            arrayListOf(Cart("Pants", "Gant pants season summer 2020", 1, 1800f)),
            1800.00f
        )
    )

    val CARDS = arrayListOf(
        CreditCard(
            1,
            R.drawable.mastercard_logo,
            "09/25",
            "John Doe",
            "Mastercard",
            "4391",
            CreditCardType.CREDIT
        ),
        CreditCard(
            2,
            R.drawable.visa_logo,
            "01/22",
            "John Doe",
            "Visa",
            "2863",
            CreditCardType.DEBIT
        )
    )
}