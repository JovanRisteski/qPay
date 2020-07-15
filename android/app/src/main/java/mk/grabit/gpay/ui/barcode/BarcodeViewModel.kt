package mk.grabit.gpay.ui.barcode

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.repository.MainRepository

class BarcodeViewModel @ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel() {

    val transactionAction = repository.transactionAction
    private val _isReady = MutableLiveData<Boolean>()
    val isReady: LiveData<Boolean>
        get() = _isReady

    init {
        _isReady.value = true
    }

    fun initPayment(paymentId: String) {
        viewModelScope.launch {
            repository.initTransaction(paymentId)
        }
    }

    fun pauseScanning(milliseconds: Long) {
        _isReady.value = false
        viewModelScope.launch {
            delay(milliseconds)
            _isReady.postValue(true)
        }
    }

    fun acknowledgeTransactionActionStatus() = repository.acknowledgeTransactionActionNone()

}