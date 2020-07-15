package mk.grabit.gpay.ui.barcode

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.data.repository.MainRepository

class BarcodeViewModel @ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel() {

    val transactionStatus = repository.status
    val transactionAction = repository.transactionAction

    fun initPayment(paymentId: String) {
        viewModelScope.launch {
            repository.initTransaction(paymentId)
        }
    }

    fun approveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.acceptTransaction(transaction)
        }
    }

    fun cancelTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.cancelTransaction(transaction)
        }
    }

    fun acknowledgeTransactionActionStatus() = repository.acknowledgeTransactionActionNone()
    fun acknowledgeTransactionStatusNone() = repository.acknowledgeTransactionStatusNone()

}