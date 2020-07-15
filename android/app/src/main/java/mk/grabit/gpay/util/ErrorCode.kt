package mk.grabit.gpay.util

object ErrorCode {

    const val ERROR = "error"
    const val CONNECTION_ERROR = "connection_error"
    const val NETWORK_ERROR = "network_error"
    const val INVALID_PAYMENT_ID = "invalid_payment_id"
    const val PAYMENT_NOT_FOUND = "payment_not_found"
    const val PAYMENT_CANCELED = "conflict_payment_status_cancelled"
}