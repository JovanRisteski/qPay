package mk.grabit.gpay.data.model

import mk.grabit.gpay.util.Helper


data class TransactionResponse(val code: String?, val timestamp: String?  = Helper.getCurrentTimestamp())