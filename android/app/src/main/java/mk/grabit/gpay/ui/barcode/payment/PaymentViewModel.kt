package mk.grabit.gpay.ui.barcode.payment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.model.CreditCard
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.data.repository.MainRepository
import mk.grabit.gpay.util.Data

class PaymentViewModel @ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel() {

    val transactionApproveStatus = repository.transactionApproveStatus
    val transactionCancelStatus = repository.transactionCancelStatus

    private val _creditCard = MutableLiveData<CreditCard>()
    val creditCard: LiveData<CreditCard>
        get() = _creditCard

    // Always choose first inserted credit card
    private val _showCardChooser = MutableLiveData<Boolean>()
    val showCardChooser: LiveData<Boolean>
        get() = _showCardChooser

    private val _navigateToDashboardDelay = MutableLiveData<Boolean>()
    val navigateToLoginDelay: LiveData<Boolean>
        get() = _navigateToDashboardDelay

    init {
        // Always choose the first inserted credit card
        _creditCard.value = Data.CARDS[0]
        _navigateToDashboardDelay.value = false
    }

    fun shouldShowCardChooser(show: Boolean) {
        _showCardChooser.value = show
    }

    fun setCreditCard(card: CreditCard) {
        _creditCard.value = card
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

    fun delayDialog(milliseconds: Long) {
        viewModelScope.launch {
            delay(milliseconds)
            _navigateToDashboardDelay.postValue(true)
        }
    }

    fun acknowledgeTransactionApproveStatusNone() = repository.acknowledgeTransactionApproveStatusNone()
    fun acknowledgeTransactionCancelStatusNone() = repository.acknowledgeTransactionCancelStatusNone()

    override fun onCleared() {
        acknowledgeTransactionApproveStatusNone()
        acknowledgeTransactionCancelStatusNone()
        super.onCleared()
    }
}