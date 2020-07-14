package mk.grabit.gpay.ui.barcode

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.repository.MainRepository

class BarcodeViewModel @ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel() {

    val transactionStatus = repository.status

    fun initPayment(paymentId: String) {
        viewModelScope.launch {
            repository.acceptTransaction(paymentId)
        }
    }

}